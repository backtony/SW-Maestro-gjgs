package com.gjgs.gjgs.modules.member.service.mypage.impl;

import com.gjgs.gjgs.modules.coupon.repositories.MemberCouponQueryRepository;
import com.gjgs.gjgs.modules.exception.member.MemberNotFoundException;
import com.gjgs.gjgs.modules.exception.payment.OrderNotFoundException;
import com.gjgs.gjgs.modules.exception.question.QuestionNotFoundException;
import com.gjgs.gjgs.modules.lecture.repositories.review.ReviewQueryRepository;
import com.gjgs.gjgs.modules.member.dto.mypage.*;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberQueryRepository;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberRepository;
import com.gjgs.gjgs.modules.member.service.mypage.interfaces.MyPageService;
import com.gjgs.gjgs.modules.notification.dto.NotificationDto;
import com.gjgs.gjgs.modules.notification.repository.interfaces.NotificationQueryRepository;
import com.gjgs.gjgs.modules.payment.repository.OrderQueryRepository;
import com.gjgs.gjgs.modules.question.repository.QuestionRepository;
import com.gjgs.gjgs.modules.reward.repository.interfaces.RewardQueryRepository;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {

    private final MemberQueryRepository memberQueryRepository;
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    private final RewardQueryRepository rewardQueryRepository;
    private final OrderQueryRepository orderQueryRepository;
    private final ReviewQueryRepository reviewQueryRepository;
    private final MemberCouponQueryRepository memberCouponQueryRepository;
    private final NotificationQueryRepository notificationQueryRepository;
    private final SecurityUtil securityUtil;

    @Override
    @Transactional(readOnly = true)
    public MyPageResponse getMyPage() {
        return MyPageResponse.from(securityUtil.getCurrentUserOrThrow());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MyBulletinDto> getMyBulletins() {
        return memberQueryRepository.findMyBulletinsByUsername(getUsernameOrThrow());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MyQuestionDto> getMyQuestions() {
        return questionRepository.findMyQuestionsByUsername(getUsernameOrThrow());
    }

    @Override
    public void editMyQuestion(Long questionId, QuestionMainTextModifyRequest mainText) {
        questionRepository.findByMemberUsernameAndId(getUsernameOrThrow(), questionId)
                .orElseThrow(() -> new QuestionNotFoundException())
                .changeMainText(mainText.getMainText());
    }

    @Override
    public void deleteMyQuestion(Long questionId) {
        if(!questionRepository.existsByMemberUsernameAndId(getUsernameOrThrow(),questionId)){
            throw new QuestionNotFoundException();
        }
        questionRepository.deleteById(questionId);
    }

    @Override
    @Transactional(readOnly = true)
    public InfoResponse getInfo(Long memberId) {
        Member member = memberQueryRepository.findWithZoneAndFavoriteCategoryAndCategoryById(memberId)
                .orElseThrow(() -> new MemberNotFoundException());
        return InfoResponse.from(member);
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<NotificationDto> getMyNotifications(Long lastNotificationId, Pageable pageable) {
        return notificationQueryRepository.findNotificationDtoListPaginationNoOffsetByUsername(getUsernameOrThrow(), lastNotificationId, pageable);
    }

    @Override
    public AlarmStatusResponse getMyAlarmStatus() {
        return memberQueryRepository.findAlarmStatusByUsername(getUsernameOrThrow());
    }

    @Override
    @Transactional(readOnly = true)
    public RewardResponse getMyRewardList(RewardPagingRequest rewardPagingRequest, Pageable pageable) {
        TotalRewardDto totalRewardDto = memberQueryRepository.findTotalRewardDtoByUsername(getUsernameOrThrow())
                .orElseThrow(() -> new MemberNotFoundException());
        Slice<RewardDto> rewardDtoSlice = rewardQueryRepository.findRewardDtoListPaginationNoOffsetByMemberIdAndRewardType
                (totalRewardDto.getMemberId(), rewardPagingRequest, pageable);
        return RewardResponse.of(rewardDtoSlice,totalRewardDto.getTotalReward());
    }

    @Override
    public void switchToDirector() {
        Member currentUser = securityUtil.getCurrentUserOrThrow();
        currentUser.changeAuthorityToDirector();
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<MyLectureResponse> getMyLectures(Pageable pageable) {
        return orderQueryRepository.findMyAppliedLectures(pageable, getUsernameOrThrow());
    }

    @Override
    @Transactional(readOnly = true)
    public TeamMemberPaymentStatusResponse getTeamOrderStatus(Long scheduleId, Long teamId) {
        return orderQueryRepository.findOrderStatusByScheduleTeamIdUsername(scheduleId, teamId, getUsernameOrThrow());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MyReviewResponse> getMyReviews(Pageable pageable) {
        return reviewQueryRepository.findMyReviews(getUsernameOrThrow(), pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public CompleteLecturePaymentResponse getCompleteLecturePayment(Long orderId) {
        return orderQueryRepository.findPaymentDetailById(orderId).orElseThrow((() -> new OrderNotFoundException()));
    }

    @Override
    @Transactional(readOnly = true)
    public MyAvailableCouponResponse getMyAvailableCoupons() {
        return memberCouponQueryRepository.findMyAvailableCouponsByUsername(getUsernameOrThrow());
    }

    private String getUsernameOrThrow() {
        return securityUtil.getCurrentUsername()
                .orElseThrow(() -> new MemberNotFoundException());
    }
}
