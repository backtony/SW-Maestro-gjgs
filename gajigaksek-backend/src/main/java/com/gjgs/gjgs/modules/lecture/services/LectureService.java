package com.gjgs.gjgs.modules.lecture.services;

import com.gjgs.gjgs.modules.bulletin.dto.search.BulletinSearchResponse;
import com.gjgs.gjgs.modules.lecture.dtos.LectureDetailResponse;
import com.gjgs.gjgs.modules.lecture.dtos.LectureQuestionsResponse;
import com.gjgs.gjgs.modules.lecture.dtos.review.ReviewResponse;
import com.gjgs.gjgs.modules.lecture.dtos.search.LectureSearchCondition;
import com.gjgs.gjgs.modules.lecture.dtos.search.LectureSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface LectureService {

    Page<LectureSearchResponse> searchLectures(Pageable pageable, LectureSearchCondition cond);

    LectureDetailResponse getLecture(Long lectureId);

    Page<BulletinSearchResponse> getBulletinsPickedLecture(Long lectureId, Pageable pageable);

    Page<LectureQuestionsResponse> getLectureQuestions(Long lectureId, Pageable pageable);

    Page<ReviewResponse> getLectureReviews(Long lectureId, Pageable pageable);

    Slice<LectureSearchResponse> getDirectorLectures(Pageable pageable, Long directorId);
}
