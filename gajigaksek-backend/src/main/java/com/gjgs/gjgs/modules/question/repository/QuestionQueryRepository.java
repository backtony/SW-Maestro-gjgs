package com.gjgs.gjgs.modules.question.repository;

import com.gjgs.gjgs.modules.lecture.dtos.LectureQuestionsResponse;
import com.gjgs.gjgs.modules.member.dto.mypage.MyQuestionDto;
import com.gjgs.gjgs.modules.question.dto.QuestionDetailResponse;
import com.gjgs.gjgs.modules.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface QuestionQueryRepository {

    List<MyQuestionDto> findMyQuestionsByUsername(String username);

    Optional<QuestionDetailResponse> findWithLectureQuestioner(Long questionId);

    Optional<Question> findWithLectureDirector(Long questionId);

    Page<LectureQuestionsResponse> findLectureQuestions(Long lectureId, Pageable pageable);
}


