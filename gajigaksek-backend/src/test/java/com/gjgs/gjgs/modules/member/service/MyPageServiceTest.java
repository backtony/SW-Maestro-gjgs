package com.gjgs.gjgs.modules.member.service;


import com.gjgs.gjgs.modules.dummy.*;
import com.gjgs.gjgs.modules.exception.question.QuestionNotFoundException;
import com.gjgs.gjgs.modules.member.dto.mypage.*;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.enums.Authority;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberQueryRepository;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberRepository;
import com.gjgs.gjgs.modules.member.service.mypage.impl.MyPageServiceImpl;
import com.gjgs.gjgs.modules.notification.dto.NotificationDto;
import com.gjgs.gjgs.modules.notification.repository.interfaces.NotificationQueryRepository;
import com.gjgs.gjgs.modules.question.entity.Question;
import com.gjgs.gjgs.modules.question.repository.QuestionRepository;
import com.gjgs.gjgs.modules.reward.enums.RewardSaveType;
import com.gjgs.gjgs.modules.reward.enums.RewardType;
import com.gjgs.gjgs.modules.reward.repository.interfaces.RewardQueryRepository;
import com.gjgs.gjgs.modules.utils.querydsl.RepositorySliceHelper;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MyPageServiceTest {

    @InjectMocks MyPageServiceImpl myPageService;
    @Mock MemberQueryRepository memberQueryRepository;
    @Mock MemberRepository memberRepository;
    @Mock QuestionRepository questionRepository;
    @Mock RewardQueryRepository rewardQueryRepository;
    @Mock NotificationQueryRepository notificationQueryRepository;
    @Mock SecurityUtil securityUtil;


    @DisplayName("MyPage 정보 가져오기")
    @Test
    void get_my_page() throws Exception {

        //given
        Member member = MemberDummy.createTestMember();
        stubGetCurrentUser(member);

        // when
        MyPageResponse dto = myPageService.getMyPage();
        // then
        assertAll(
                () -> assertEquals(member.getNickname(),dto.getNickname()),
                () -> assertEquals(member.getMemberCategories().size(),dto.getMemberCategoryIdList().size()),
                () -> assertEquals(member.getTotalReward(),dto.getTotalReward()),
                () -> assertEquals(member.getAuthority(),dto.getAuthority()),
                () -> assertEquals(member.getImageFileUrl(),dto.getImageFileUrl())
        );
    }


    @DisplayName("내 게시글 가져오기")
    @Test
    void get_my_bulletins() throws Exception {
        //given
        List<MyBulletinDto> myBulletinRespons = Arrays.asList(BulletinDummy.createBulletinDto());

        stubGetCurrentUsername("test");
        when(memberQueryRepository.findMyBulletinsByUsername(any())).thenReturn(myBulletinRespons);

        //when then
        assertEquals(myBulletinRespons, myPageService.getMyBulletins());
    }

    @DisplayName("내 문의글 가져오기")
    @Test
    void get_my_questions() throws Exception {
        //given
        List<MyQuestionDto> myQuestionDtoList = Arrays.asList(QuestionDummy.createMyQuestionDto());

        stubGetCurrentUsername("test");
        when(questionRepository.findMyQuestionsByUsername(any())).thenReturn(myQuestionDtoList);

        //when then
        assertEquals(myQuestionDtoList, myPageService.getMyQuestions());
    }

    @DisplayName("내 문의글 수정하기")
    @Test
    void edit_my_question() throws Exception {
        //given
        Question question = createQuestion();
        QuestionMainTextModifyRequest questionMainTextModifyRequest = QuestionMainTextModifyRequest.from("change Main Text");

        stubGetCurrentUsername("test");
        when(questionRepository.findByMemberUsernameAndId(any(),any())).thenReturn(Optional.of(question));

        //when
        myPageService.editMyQuestion( 1L, questionMainTextModifyRequest);

        //then
        assertEquals(question.getMainText(), questionMainTextModifyRequest.getMainText());
    }

    private Question createQuestion() {
        return QuestionDummy.createWaitQuestion(LectureDummy.createLecture(1,
                MemberDummy.createTestDirectorMember()),
                MemberDummy.createTestMember());
    }

    @DisplayName("타인이 작성한 문의글을 수정하려고 하는 경우")
    @Test
    void edit_not_my_question() throws Exception {
        //given
        QuestionMainTextModifyRequest questionMainTextModifyRequest = QuestionMainTextModifyRequest.from("change Main Text");

        stubGetCurrentUsername("test");
        when(questionRepository.findByMemberUsernameAndId(any(),any())).thenReturn(Optional.empty());

        //when then
        assertThrows(QuestionNotFoundException.class,
                () -> myPageService.editMyQuestion(1L, questionMainTextModifyRequest));


    }



    @DisplayName("문의글 삭제하기")
    @Test
    void delete_my_question() throws Exception {
        //given
        stubGetCurrentUsername("test");
        when(questionRepository.existsByMemberUsernameAndId(any(),any())).thenReturn(true);

        //when
        myPageService.deleteMyQuestion( 1L);

        // then
        verify(questionRepository, times(1)).deleteById(any());
    }

    @DisplayName("타인의 문의글 삭제시도할 경우")
    @Test
    void delete_not_my_question() throws Exception {
        //given
        stubGetCurrentUsername("test");
        when(questionRepository.existsByMemberUsernameAndId(any(),any())).thenReturn(false);

        //when then
        assertThrows(QuestionNotFoundException.class,
                () -> myPageService.deleteMyQuestion(1L));

    }

    @DisplayName("회원 info 창")
    @Test
    void get_Info() throws Exception {
        //given
        Member member = MemberDummy.createTestMember();
        when(memberQueryRepository.findWithZoneAndFavoriteCategoryAndCategoryById(any())).thenReturn(Optional.of(member));

        //when
        InfoResponse info = myPageService.getInfo(1L);

        //then
        assertAll(
                () -> assertEquals(member.getImageFileUrl(), info.getImageFileUrl()),
                () -> assertEquals(member.getSex(), info.getSex()),
                () -> assertEquals(member.getAge(), info.getAge()),
                () -> assertEquals(member.getZone().getId(), info.getZoneId()),
                () -> assertEquals(member.getMemberCategories().size(), info.getCategoryIdList().size()),
                () -> assertEquals(member.getProfileText(), info.getProfileText()),
                () -> assertEquals(member.getNickname(), info.getNickname())
        );

    }


    @DisplayName("내 모든 알림 가져오기")
    @Test
    void get_my_notifications() throws Exception{
        //given
        stubGetCurrentUsername("test");
        List<NotificationDto> notificationDtoList = NotificationDummy.createNotificationDtoList();
        PageRequest pageRequest = PageRequest.of(0, 10);
        Slice<NotificationDto> notificationDtoSlice = RepositorySliceHelper.toSlice(notificationDtoList, pageRequest);
        when(notificationQueryRepository.findNotificationDtoListPaginationNoOffsetByUsername(any(),any(),any())).thenReturn(notificationDtoSlice);

        //when
        Slice<NotificationDto> results = myPageService.getMyNotifications(10L, pageRequest);

        //then
        assertEquals(2, results.getNumberOfElements());
    }



    @DisplayName("리워드 상세 조회")
    @Test
    void get_my_rewardList() throws Exception{
        //given
        Member member = MemberDummy.createTestMember();
        TotalRewardDto totalRewardDto = TotalRewardDto.of(1L, member.getTotalReward());
        RewardPagingRequest rewardPagingRequest = RewardPagingRequest.of(20L,RewardType.SAVE);
        PageRequest pageRequest = PageRequest.of(0, 20);
        List<RewardDto> rewardDtoList = RewardDummy.createRewardDtoList();
        Slice<RewardDto> rewardDtoSlice = RepositorySliceHelper.toSlice(rewardDtoList, pageRequest);

        stubGetCurrentUsername("test");
        when(memberQueryRepository.findTotalRewardDtoByUsername(any()))
                .thenReturn(Optional.of(totalRewardDto));
        when(rewardQueryRepository.findRewardDtoListPaginationNoOffsetByMemberIdAndRewardType(any(),any(),any()))
                .thenReturn(rewardDtoSlice);

        //when
        RewardResponse response = myPageService.getMyRewardList(rewardPagingRequest, pageRequest);

        //then
        Slice<RewardDto> slice = response.getRewardDtoSlice();
        List<RewardDto> content = slice.getContent();
        assertAll(
                () -> assertEquals(totalRewardDto.getTotalReward(),response.getTotalReward()),
                () -> assertEquals(20,content.size()),
                () -> assertEquals(RewardSaveType.RECOMMEND.getRewardType(), content.get(0).getRewardType()),
                () -> assertEquals(RewardSaveType.RECOMMEND.getAmount(),content.get(0).getAmount()),
                () -> assertEquals(RewardSaveType.RECOMMEND.getText(),content.get(0).getText()),
                () -> assertEquals(20,slice.getSize()),
                () -> assertEquals(20,slice.getNumberOfElements()),
                () -> assertEquals(0,slice.getNumber())
        );
    }

    @DisplayName("디렉터로 전환")
    @Test
    void switch_to_director() throws Exception{
        //given
        Member member = MemberDummy.createTestMember();
        stubGetCurrentUser(member);

        //when
        myPageService.switchToDirector();

        //then
        assertEquals(Authority.ROLE_DIRECTOR,member.getAuthority());
    }

    @DisplayName("내 알림 수신 여부 상태 확인")
    @Test
    void get_my_alarm_status() throws Exception{
        //given
        stubGetCurrentUsername("test");

        //when
        myPageService.getMyAlarmStatus();

        //then
        verify(memberQueryRepository,times(1)).findAlarmStatusByUsername(any());
    }


    private void stubGetCurrentUsername(String test) {
        when(securityUtil.getCurrentUsername()).thenReturn(Optional.of(test));
    }


    private void stubGetCurrentUser(Member member) {
        when(securityUtil.getCurrentUserOrThrow()).thenReturn(member);
    }


}
