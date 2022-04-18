package com.gjgs.gjgs.modules.member.service;

import com.gjgs.gjgs.modules.dummy.AlarmDummy;
import com.gjgs.gjgs.modules.dummy.FileDummy;
import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.member.dto.myinfo.*;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.enums.Alarm;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberCategoryRepository;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberJdbcRepository;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberRepository;
import com.gjgs.gjgs.modules.member.service.mypage.impl.MyInfoEditServiceImpl;
import com.gjgs.gjgs.modules.utils.s3.FileManager;
import com.gjgs.gjgs.modules.utils.s3.interfaces.AmazonS3Service;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MyInfoEditServiceImplTest {

    @InjectMocks MyInfoEditServiceImpl myInfoEditService;
    @Mock MemberRepository memberRepository;
    @Mock AmazonS3Service amazonS3Service;
    @Mock FileManager fileManager;
    @Mock MemberCategoryRepository memberCategoryRepository;
    @Mock MemberJdbcRepository memberJdbcRepository;
    @Mock SecurityUtil securityUtil;


    Member member;

    @BeforeEach
    void setup(){
        member = MemberDummy.createTestMember();
    }

    @DisplayName("editMyPage 정보 가져오기")
    @Test
    void edit_my_page() throws Exception {

        //given
        when(securityUtil.getCurrentUsername()).thenReturn(Optional.of(member.getUsername()));
        when(memberRepository.findWithMemberCategoryByUsername(any())).thenReturn(Optional.of(member));

        // when
        myInfoEditService.editMyPage();

        // then
        assertThat(myInfoEditService.editMyPage()).isInstanceOf(MyPageEditResponse.class);


    }

    @DisplayName("닉네임 수정")
    @Test
    void edit_nickname() throws Exception {

        //given
        stubGetCurrentUser();
        NicknameModifyRequest nicknameModifyRequest = createNicknameModifyRequest();

        // when
        myInfoEditService.editNickname(nicknameModifyRequest);

        // then
        assertEquals(nicknameModifyRequest.getNickname(), member.getNickname());
    }

    private NicknameModifyRequest createNicknameModifyRequest() {
        return NicknameModifyRequest.builder()
                .nickname("changeNickname")
                .build();
    }

    @DisplayName("휴대폰번호 수정")
    @Test
    void edit_phone() throws Exception {

        //given
        stubGetCurrentUser();
        PhoneModifyRequest phoneModifyRequest = createPhoneModifyRequest();

        // when
        myInfoEditService.editPhone(phoneModifyRequest);

        // then
        assertEquals(phoneModifyRequest.getPhone(), member.getPhone());
    }

    private PhoneModifyRequest createPhoneModifyRequest() {
        return PhoneModifyRequest.builder()
                .phone("11111111111")
                .build();
    }

    @DisplayName("선호 카테고리가 존재하는 상태에서 선호 카테고리 수정 ")
    @Test
    void edit_category_already_exists() throws Exception {

        //given
        when(memberRepository.findIdByUsername(any())).thenReturn(Optional.of(1L));
        when(securityUtil.getCurrentUsername()).thenReturn(Optional.of(member.getUsername()));
        when(memberCategoryRepository.existsByMemberId(any())).thenReturn(true);

        // when
        myInfoEditService.editCategory(Arrays.asList(1L, 2L));

        // then
        assertAll(
                () -> verify(memberCategoryRepository,times(1)).existsByMemberId(any()),
                () -> verify(memberCategoryRepository,times(1)).deleteAllWithBulkByMemberId(any()),
                () -> verify(memberJdbcRepository,times(1)).insertMemberCategoryList(any(),any())
        );
    }

    @DisplayName("선호 카테고리가 존재하는 않는 상태애서 선호 카테고리 수정 ")
    @Test
    void edit_category_not_exsits() throws Exception {

        //given
        when(memberRepository.findIdByUsername(any())).thenReturn(Optional.of(1L));
        when(memberCategoryRepository.existsByMemberId(any())).thenReturn(false);
        when(securityUtil.getCurrentUsername()).thenReturn(Optional.of(member.getUsername()));

        // when
        myInfoEditService.editCategory(Arrays.asList(1L, 2L));

        // then
        assertAll(
                () -> verify(memberCategoryRepository,times(1)).existsByMemberId(any()),
                () -> verify(memberCategoryRepository,times(0)).deleteAllWithBulkByMemberId(any()),
                () -> verify(memberJdbcRepository,times(1)).insertMemberCategoryList(any(),any())
        );
    }

    @DisplayName("profileText 수정")
    @Test
    void edit_profileText() throws Exception {

        //given
        stubGetCurrentUser();
        ProfileTextModifyRequest profileTextModifyRequest = createProfileTextModifyRequest();

        // when
        myInfoEditService.editProfileText(profileTextModifyRequest);

        // then
        assertEquals(profileTextModifyRequest.getProfileText(), member.getProfileText());
    }

    private ProfileTextModifyRequest createProfileTextModifyRequest() {
        String text = "hello world";
        ProfileTextModifyRequest profileTextModifyRequest = ProfileTextModifyRequest.from(text);
        return profileTextModifyRequest;
    }

    @DisplayName("zone 수정")
    @Test
    void edit_zone() throws Exception {

        //given
        stubGetCurrentUser();

        // when
        myInfoEditService.editZone(100L);

        // then
        assertEquals(100L, member.getZone().getId());
    }


    @DisplayName("이미지 수정 - 카카오 url인 경우")
    @Test
    void edit_image_kakao() throws Exception {
        //given
        stubGetCurrentUser();
        String fileName = "fileName";
        String uploadImageUrl = "uploadImageUrl";
        String kakaoUrl = "http://k.kakaocdn.net";
        MockMultipartFile file = FileDummy.getFiles();

        when(fileManager.getUploadedFileName(any(), any())).thenReturn(fileName);
        when(fileManager.upload(any(), any(), any())).thenReturn(uploadImageUrl);
        ReflectionTestUtils.setField(myInfoEditService, "kakaoImageUrl", kakaoUrl);

        //when
        myInfoEditService.editImage(file);

        //then
        assertAll(
                () -> assertEquals(fileName, member.getImageFileName()),
                () -> assertEquals(uploadImageUrl, member.getImageFileUrl())
        );


    }


    @DisplayName("이미지 수정 - 일반 이미지인 경우")
    @Test
    void edit_image_normal() throws Exception{
        // given
        stubGetCurrentUser();
        String fileName = "fileName";
        String uploadImageUrl = "uploadImageUrl";
        String kakaoUrl = "http://k.kakaocdn.net";
        MockMultipartFile file = FileDummy.getFiles();
        member.changeFileInfo("test", "test");

        when(fileManager.getUploadedFileName(any(), any())).thenReturn(fileName);
        when(fileManager.upload(any(), any(), any())).thenReturn(uploadImageUrl);
        ReflectionTestUtils.setField(myInfoEditService, "kakaoImageUrl", kakaoUrl);

        // when
        myInfoEditService.editImage(file);

        //then
        assertAll(
                () -> assertEquals(fileName, member.getImageFileName()),
                () -> assertEquals(uploadImageUrl, member.getImageFileUrl()),
                () -> verify(amazonS3Service).delete(any(), any())
        );
    }

    @DisplayName("이벤트 수신 여부 변경")
    @Test
    void edit_my_event_alarm() throws Exception{
        //given
        stubGetCurrentUser();
        AlarmEditRequest alarmEditRequest = AlarmDummy.createAlarmEditRequest(false,Alarm.EVENT);

        //when
        myInfoEditService.editMyEventAlarm(alarmEditRequest);

        //then
        assertFalse(member.isEventAlarm());
    }



    private void stubGetCurrentUser() {
        when(securityUtil.getCurrentUserOrThrow()).thenReturn(member);
    }
}
