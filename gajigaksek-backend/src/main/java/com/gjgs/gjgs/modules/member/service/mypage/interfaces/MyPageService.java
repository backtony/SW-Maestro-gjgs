package com.gjgs.gjgs.modules.member.service.mypage.interfaces;


import com.gjgs.gjgs.modules.member.dto.mypage.*;
import com.gjgs.gjgs.modules.notification.dto.NotificationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface MyPageService {
    MyPageResponse getMyPage();

    List<MyBulletinDto> getMyBulletins();

    List<MyQuestionDto> getMyQuestions();

    void editMyQuestion(Long questionId, QuestionMainTextModifyRequest mainText);

    void deleteMyQuestion(Long questionId);

    InfoResponse getInfo(Long memberId);

    void switchToDirector();

    RewardResponse getMyRewardList(RewardPagingRequest rewardPagingRequest, Pageable pageable);

    Slice<MyLectureResponse> getMyLectures(Pageable pageable);

    TeamMemberPaymentStatusResponse getTeamOrderStatus(Long scheduleId, Long teamId);

    Page<MyReviewResponse> getMyReviews(Pageable pageable);

    CompleteLecturePaymentResponse getCompleteLecturePayment(Long orderId);

    MyAvailableCouponResponse getMyAvailableCoupons();

    Slice<NotificationDto> getMyNotifications(Long lastNotificationId, Pageable pageable);

    AlarmStatusResponse getMyAlarmStatus();
}
