package com.gjgs.gjgs.modules.lecture.controllers;

import com.gjgs.gjgs.modules.bulletin.dto.search.BulletinSearchResponse;
import com.gjgs.gjgs.modules.lecture.dtos.LectureDetailResponse;
import com.gjgs.gjgs.modules.lecture.dtos.LectureQuestionsResponse;
import com.gjgs.gjgs.modules.lecture.dtos.review.ReviewResponse;
import com.gjgs.gjgs.modules.lecture.dtos.search.LectureSearchCondition;
import com.gjgs.gjgs.modules.lecture.dtos.search.LectureSearchResponse;
import com.gjgs.gjgs.modules.lecture.services.LectureService;
import com.gjgs.gjgs.modules.utils.validators.search.CheckKeyword;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lectures")
@Validated
public class LectureQueryController {

    private final LectureService lectureService;

    @GetMapping("")
    public ResponseEntity<Page<LectureSearchResponse>> getLectures(Pageable pageable,
                                                                   @CheckKeyword LectureSearchCondition condition) {
        return new ResponseEntity<>(lectureService.searchLectures(pageable, condition), HttpStatus.OK);
    }


    @GetMapping("/{lectureId}")
    public ResponseEntity<LectureDetailResponse> getLecture(@PathVariable("lectureId") Long lectureId) {
        return new ResponseEntity<>(lectureService.getLecture(lectureId), HttpStatus.OK);
    }


    @GetMapping("/{lectureId}/questions")
    public ResponseEntity<Page<LectureQuestionsResponse>> getLectureQuestions(@PathVariable Long lectureId, Pageable pageable) {
        return new ResponseEntity<>(lectureService.getLectureQuestions(lectureId, pageable), HttpStatus.OK);
    }

    @GetMapping("/{lectureId}/bulletins")
    public ResponseEntity<Page<BulletinSearchResponse>> getBulletinsPickedLecture(@PathVariable("lectureId") Long lectureId,
                                                                                  Pageable pageable) {
        return new ResponseEntity<>(lectureService.getBulletinsPickedLecture(lectureId, pageable), HttpStatus.OK);
    }

    @GetMapping("/{lectureId}/reviews")
    public ResponseEntity<Page<ReviewResponse>> getLectureReviews(@PathVariable("lectureId") Long lectureId,
                                                                  Pageable pageable) {
        return ResponseEntity.ok(lectureService.getLectureReviews(lectureId, pageable));
    }

    @GetMapping("/directors/{directorId}")
    public ResponseEntity<Slice<LectureSearchResponse>> getDirectorLectures(Pageable pageable,
                                                                            @PathVariable Long directorId) {
        return ResponseEntity.ok(lectureService.getDirectorLectures(pageable, directorId));
    }
}
