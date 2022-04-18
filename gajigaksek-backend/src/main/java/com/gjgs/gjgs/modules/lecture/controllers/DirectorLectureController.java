package com.gjgs.gjgs.modules.lecture.controllers;

import com.gjgs.gjgs.modules.coupon.dto.DirectorCouponResponse;
import com.gjgs.gjgs.modules.coupon.services.DirectorCouponService;
import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLecture;
import com.gjgs.gjgs.modules.lecture.dtos.director.lecture.DirectorLectureResponse;
import com.gjgs.gjgs.modules.lecture.dtos.director.lecture.GetLectureType;
import com.gjgs.gjgs.modules.lecture.dtos.director.question.DirectorQuestionResponse;
import com.gjgs.gjgs.modules.lecture.dtos.director.question.DirectorQuestionSearchCondition;
import com.gjgs.gjgs.modules.lecture.dtos.director.schedule.DirectorLectureScheduleResponse;
import com.gjgs.gjgs.modules.lecture.dtos.director.schedule.DirectorScheduleResponse;
import com.gjgs.gjgs.modules.lecture.dtos.director.schedule.DirectorScheduleSearchCondition;
import com.gjgs.gjgs.modules.lecture.services.director.lecture.DirectorLectureService;
import com.gjgs.gjgs.modules.lecture.services.director.schedule.DirectorScheduleService;
import com.gjgs.gjgs.modules.utils.validators.search.CheckKeyword;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mypage/directors")
@Validated
@PreAuthorize("hasAnyRole('DIRECTOR')")
public class DirectorLectureController {

    private final DirectorLectureService directorLectureService;
    private final DirectorScheduleService directorScheduleService;
    private final DirectorCouponService directorCouponService;

    @GetMapping("/lectures")
    public ResponseEntity<DirectorLectureResponse> getDirectorLectures(@RequestParam("condition") GetLectureType condition) {
        return ResponseEntity.ok(directorLectureService.getDirectorLectures(condition));
    }

    @PatchMapping("/lectures/{lectureId}")
    public ResponseEntity<Void> rewriteLecture(@PathVariable Long lectureId) {
        directorLectureService.changeLectureCreating(lectureId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lectures/{lectureId}")
    public ResponseEntity<Void> deleteRejectLecture(@PathVariable Long lectureId) {
        directorLectureService.deleteRejectLecture(lectureId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/schedules")
    public ResponseEntity<Page<DirectorLectureScheduleResponse>> getDirectorSchedules(Pageable pageable,
                                                                                      @CheckKeyword @Valid DirectorScheduleSearchCondition condition) {
        return ResponseEntity.ok(directorLectureService.getDirectorLecturesSchedules(pageable, condition));
    }

    @GetMapping("/lectures/{lectureId}/schedules")
    public ResponseEntity<DirectorScheduleResponse> getLectureSchedules(@PathVariable Long lectureId) {
        return ResponseEntity.ok(directorScheduleService.getLectureSchedules(lectureId));
    }

    @PostMapping("/lectures/{lectureId}/schedules")
    public ResponseEntity<DirectorScheduleResponse.PostDelete> createSchedule(@PathVariable Long lectureId,
                                                                                     @RequestBody @Validated CreateLecture.ScheduleDto scheduleDto) {
        return ResponseEntity.ok(directorScheduleService.createSchedule(lectureId, scheduleDto));
    }

    @DeleteMapping("/lectures/{lectureId}/schedules/{scheduleId}")
    public ResponseEntity<DirectorScheduleResponse.PostDelete> deleteSchedule(@PathVariable Long lectureId, @PathVariable Long scheduleId) {
        return ResponseEntity.ok(directorScheduleService.deleteSchedule(lectureId, scheduleId));
    }

    @GetMapping("/questions")
    public ResponseEntity<Page<DirectorQuestionResponse>> getDirectorQuestions(Pageable pageable,
                                                                               @Valid DirectorQuestionSearchCondition condition) {
        return ResponseEntity.ok(directorLectureService.getDirectorQuestions(pageable, condition));
    }

    @GetMapping("/coupons")
    public ResponseEntity<DirectorCouponResponse> getDirectorCoupons() {
        return ResponseEntity.ok(directorCouponService.getDirectorCoupons());
    }

    @PostMapping("/coupons/{lectureId}")
    public ResponseEntity<Void> issueCoupon(@PathVariable Long lectureId,
                                            @RequestBody CreateLecture.CouponDto couponDto) {
        directorCouponService.issue(lectureId, couponDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/coupons/{lectureId}")
    public ResponseEntity<Void> closeCoupon(@PathVariable Long lectureId) {
        directorCouponService.close(lectureId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
