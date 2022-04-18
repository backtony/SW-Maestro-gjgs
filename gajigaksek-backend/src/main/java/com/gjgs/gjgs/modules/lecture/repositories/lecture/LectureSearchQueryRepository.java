package com.gjgs.gjgs.modules.lecture.repositories.lecture;

import com.gjgs.gjgs.modules.lecture.dtos.director.question.DirectorQuestionResponse;
import com.gjgs.gjgs.modules.lecture.dtos.director.question.DirectorQuestionSearchCondition;
import com.gjgs.gjgs.modules.lecture.dtos.director.schedule.DirectorLectureScheduleResponse;
import com.gjgs.gjgs.modules.lecture.dtos.director.schedule.DirectorScheduleSearchCondition;
import com.gjgs.gjgs.modules.lecture.dtos.review.ReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LectureSearchQueryRepository {

    Page<DirectorLectureScheduleResponse> findSchedulesByDirectorUsername(String username, Pageable pageable, DirectorScheduleSearchCondition condition);

    Page<DirectorQuestionResponse> findQuestionsByDirectorUsername(String username, Pageable pageable, DirectorQuestionSearchCondition condition);

    Page<ReviewResponse> findReviewsByLectureId(Long lectureId, Pageable pageable);
}
