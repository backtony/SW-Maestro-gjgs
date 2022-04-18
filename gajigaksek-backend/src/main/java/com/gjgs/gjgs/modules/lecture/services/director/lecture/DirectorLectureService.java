package com.gjgs.gjgs.modules.lecture.services.director.lecture;

import com.gjgs.gjgs.modules.lecture.dtos.director.lecture.DirectorLectureResponse;
import com.gjgs.gjgs.modules.lecture.dtos.director.lecture.GetLectureType;
import com.gjgs.gjgs.modules.lecture.dtos.director.question.DirectorQuestionResponse;
import com.gjgs.gjgs.modules.lecture.dtos.director.question.DirectorQuestionSearchCondition;
import com.gjgs.gjgs.modules.lecture.dtos.director.schedule.DirectorLectureScheduleResponse;
import com.gjgs.gjgs.modules.lecture.dtos.director.schedule.DirectorScheduleSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DirectorLectureService {

    DirectorLectureResponse getDirectorLectures(GetLectureType type);

    Page<DirectorLectureScheduleResponse> getDirectorLecturesSchedules(Pageable pageable, DirectorScheduleSearchCondition condition);

    Page<DirectorQuestionResponse> getDirectorQuestions(Pageable pageable, DirectorQuestionSearchCondition condition);

    void deleteRejectLecture(Long lectureId);

    void changeLectureCreating(Long lectureId);
}
