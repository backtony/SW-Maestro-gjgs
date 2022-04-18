package com.gjgs.gjgs.modules.member.entity;

import com.gjgs.gjgs.modules.category.entity.Category;
import com.gjgs.gjgs.modules.dummy.CategoryDummy;
import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.dummy.ZoneDummy;
import com.gjgs.gjgs.modules.exception.reward.RewardException;
import com.gjgs.gjgs.modules.member.dto.login.SignUpRequest;
import com.gjgs.gjgs.modules.member.enums.Alarm;
import com.gjgs.gjgs.modules.member.enums.Authority;
import com.gjgs.gjgs.modules.member.enums.Sex;
import com.gjgs.gjgs.modules.zone.entity.Zone;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class
MemberTest {

    @DisplayName("queryDsl용 member 엔티티 생성")
    @Test
    void of_only_id() throws Exception{
        //given
        Member member = Member.from(1L);

        //when then
        assertEquals(1L,member.getId());
    }

    @DisplayName("nickname, id, fcmtoken을 갖는 member 생성")
    @Test
    void create_member_include_nickName_id_fcmToken() throws Exception{
        //given
        Member member = Member.of(1L, "fcm", "nick");
        //when then
        assertAll(
                () -> assertEquals(1L,member.getId()),
                () -> assertEquals("fcm",member.getFcmToken()),
                () -> assertEquals("nick",member.getNickname())
        );

        //then
    }

    @DisplayName("회원 생성")
    @Test
    void create_member() throws Exception {
        //given
        SignUpRequest signUpRequest = MemberDummy.createSignupForm();

        //when
        Member member = Member.from(signUpRequest);

        //then
        assertEquals(member.getName(), signUpRequest.getName());
        assertEquals(member.getPhone(), signUpRequest.getPhone());
        assertEquals(member.getNickname(), signUpRequest.getNickname());
        assertEquals(member.getAge(), signUpRequest.getAge());
        assertEquals(member.getSex(), Sex.valueOf(signUpRequest.getSex()));
        assertEquals(member.getZone().getId(), signUpRequest.getZoneId());
        assertEquals(member.getImageFileUrl(), signUpRequest.getImageFileUrl());
        assertEquals(member.getUsername(), String.valueOf(signUpRequest.getId()));
        assertEquals(member.getAuthority(), Authority.ROLE_USER);
        assertEquals(member.getFcmToken(), signUpRequest.getFcmToken());

    }

    @DisplayName("멤버 선호 카테고리 수정")
    @Test
    void change_memberCategories() throws Exception {
        //given
        Member member = MemberDummy.createTestMember();
        Category category = CategoryDummy.createCategory();
        List<Category> categoryList = Arrays.asList(category);

        //when
        member.setMemberCategories(categoryList);

        //then
        assertEquals(member.getMemberCategories().get(0).getCategory(), category);
    }

    @DisplayName("닉네임 수정")
    @Test
    void change_nickname() throws Exception {
        //given
        Member member = MemberDummy.createTestMember();

        //when
        member.changeNickname("hello");

        //then
        assertEquals("hello", member.getNickname());
    }

    @DisplayName("소개 수정")
    @Test
    void change_profileText() throws Exception {
        //given
        Member member = MemberDummy.createTestMember();

        //when
        member.changeProfileText("hello text");

        //then
        assertEquals("hello text", member.getProfileText());
    }

    @DisplayName("휴대폰 수정")
    @Test
    void change_phone() throws Exception {
        //given
        Member member = MemberDummy.createTestMember();

        //when
        member.changePhone("11111111111");

        //then
        assertEquals("11111111111", member.getPhone());
    }

    @DisplayName("디렉터 소개 수정")
    @Test
    void change_directorText() throws Exception {
        //given
        Member member = MemberDummy.createTestMember();

        //when
        member.changeDirectorText("test directorText");

        //then
        assertEquals("test directorText", member.getDirectorText());
    }

    @DisplayName("image file info 수정")
    @Test
    void add_fileInfo() throws Exception {
        //given
        Member member = MemberDummy.createTestMember();

        //when
        member.changeFileInfo("testFileName", "testFileUrl");

        //then
        assertAll(
                () -> assertEquals("testFileName", member.getImageFileName()),
                () -> assertEquals("testFileUrl", member.getImageFileUrl())
        );
    }

    @DisplayName("zone 수정")
    @Test
    void change_zone() throws Exception {
        //given
        Member member = MemberDummy.createTestMember();
        Zone zone = ZoneDummy.createZone(2L);

        //when
        member.changeZone(zone);

        //then
        assertEquals(zone, member.getZone());
    }

    @DisplayName("FcmToken 수정")
    @Test
    void change_fcmToken() throws Exception{
        //given
        Zone zone = ZoneDummy.createZone();
        SignUpRequest signUpRequest = MemberDummy.createSignupForm();
        Member member = Member.from(signUpRequest);
        String changeFcm = "changeFcmToken";

        //when
        member.changeFcmToken(changeFcm);

        //then
        assertEquals(changeFcm,member.getFcmToken());
    }


    @DisplayName("FcmToken 삭제")
    @Test
    void clear_fcmToken() throws Exception{
        //given
        Zone zone = ZoneDummy.createZone();
        SignUpRequest signUpRequest = MemberDummy.createSignupForm();
        Member member = Member.from(signUpRequest);

        //when
        member.clearFcmToken();

        //then
        assertEquals("",member.getFcmToken());
    }


    @DisplayName("디렉터로 전환")
    @Test
    void change_authority_to_director() throws Exception{
        //given
        Member member = MemberDummy.createTestMember();

        //when
        member.changeAuthorityToDirector();

        //then
        assertEquals(Authority.ROLE_DIRECTOR,member.getAuthority());
    }

    @DisplayName("리워드가 부족할 때 예외 발생")
    @Test
    void reward_exception_test()throws Exception {

        // given
        Member member = Member.builder().totalReward(1000).build();

        // when, then
        assertThrows(RewardException.class,
                () -> member.useReward(1200),
                "리워드가 부족할 경우 예외 발생");
    }

    @DisplayName("리워드 사용")
    @Test
    void use_reward_exception_test()throws Exception {

        // given
        Member member = Member.builder().totalReward(1000).build();

        // when
        member.useReward(900);

        // then
        assertEquals(member.getTotalReward(), 100);
    }


    @DisplayName("이벤트 수신 여부 변경")
    @Test
    void change_event_alarm() throws Exception{
        //given
        Member member = Member.builder()
                .eventAlarm(true)
                .build();

        //when
        member.changeAlarm(Alarm.EVENT,false);

        //then
        assertFalse(member.isEventAlarm());

    }
}

