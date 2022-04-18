package com.gjgs.gjgs.modules.member.controller;


import com.gjgs.gjgs.modules.member.dto.mypage.*;
import com.gjgs.gjgs.modules.member.service.mypage.interfaces.MyPageService;
import com.gjgs.gjgs.modules.notification.dto.NotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;


    @PreAuthorize("hasAnyRole('USER,DIRECTOR')")
    @GetMapping
    public ResponseEntity<MyPageResponse> myPage() {
        return ResponseEntity.ok(myPageService.getMyPage());
    }


    @PreAuthorize("hasAnyRole('USER,DIRECTOR')")
    @GetMapping("/bulletins")
    public ResponseEntity<MyBulletinResponse> myBulletins() {
        return ResponseEntity.ok(MyBulletinResponse.from(myPageService.getMyBulletins()));
    }


    @PreAuthorize("hasAnyRole('USER,DIRECTOR')")
    @GetMapping("/question")
    public ResponseEntity<MyQuestionResponse> myQuestion() {
        return ResponseEntity.ok(MyQuestionResponse.from(myPageService.getMyQuestions()));
    }


    @PreAuthorize("hasAnyRole('USER,DIRECTOR')")
    @PatchMapping("/question/{questionId}")
    public ResponseEntity<Void> editMyQuestion(@PathVariable("questionId") Long questionId,
                                               @RequestBody @Valid QuestionMainTextModifyRequest mainText) {
        myPageService.editMyQuestion(questionId, mainText);
        return ResponseEntity.ok().build();
    }


    @PreAuthorize("hasAnyRole('USER,DIRECTOR')")
    @DeleteMapping("/question/{questionId}")
    public ResponseEntity<Void> deleteMyQuestion(@PathVariable("questionId") Long questionId) {
        myPageService.deleteMyQuestion(questionId);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/info/{memberId}")
    public ResponseEntity<InfoResponse> info(@PathVariable("memberId") Long memberId) {
        return ResponseEntity.ok(myPageService.getInfo(memberId));
    }

    @PreAuthorize("hasAnyRole('USER,DIRECTOR')")
    @GetMapping("/notifications")
    public ResponseEntity<Slice<NotificationDto>> getMyNotifications(@PageableDefault(size = 20) Pageable pageable,
                                                                     Long lastNotificationId){
        return ResponseEntity.ok(myPageService.getMyNotifications(lastNotificationId, pageable));
    }

    @PreAuthorize("hasAnyRole('USER,DIRECTOR')")
    @GetMapping("/reward")
    public ResponseEntity<RewardResponse> getMyReward(@Validated RewardPagingRequest rewardPagingRequest,
                                                      @PageableDefault(size = 20) Pageable pageable){
        return ResponseEntity.ok(myPageService.getMyRewardList(rewardPagingRequest,pageable));
    }

    @PreAuthorize("hasAnyRole('USER,DIRECTOR')")
    @GetMapping("/lectures")
    public ResponseEntity<Slice<MyLectureResponse>> getMyLectures(Pageable pageable) {
        return ResponseEntity.ok(myPageService.getMyLectures(pageable));
    }

    @PreAuthorize("hasAnyRole('USER,DIRECTOR')")
    @GetMapping("/payment/{orderId}")
    public ResponseEntity<CompleteLecturePaymentResponse> getMyPaymentLecture(@PathVariable Long orderId) {
        return ResponseEntity.ok(myPageService.getCompleteLecturePayment(orderId));
    }

    @PreAuthorize("hasAnyRole('USER,DIRECTOR')")
    @GetMapping("/lectures/payment/{scheduleId}/teams/{teamId}")
    public ResponseEntity<TeamMemberPaymentStatusResponse> getTeamOrderStatus(@PathVariable Long scheduleId,
                                                                         @PathVariable Long teamId) {
        return ResponseEntity.ok(myPageService.getTeamOrderStatus(scheduleId, teamId));
    }

    @PreAuthorize("hasAnyRole('USER,DIRECTOR')")
    @GetMapping("/reviews")
    public ResponseEntity<Page<MyReviewResponse>> getMyReviews(Pageable pageable) {
        return ResponseEntity.ok(myPageService.getMyReviews(pageable));
    }

    @PreAuthorize("hasAnyRole('USER,DIRECTOR')")
    @GetMapping("/coupons")
    public ResponseEntity<MyAvailableCouponResponse> getMyCoupons() {
        return ResponseEntity.ok(myPageService.getMyAvailableCoupons());
    }

    @GetMapping("/alarm")
    public ResponseEntity<AlarmStatusResponse> getMyEventAlarm(){
        return ResponseEntity.ok(myPageService.getMyAlarmStatus());
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/switch-director")
    public ResponseEntity<Void> switchToDirector (){
        myPageService.switchToDirector();
        return ResponseEntity.ok().build();
    }

}
