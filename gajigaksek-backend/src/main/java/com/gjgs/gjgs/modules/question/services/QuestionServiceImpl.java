package com.gjgs.gjgs.modules.question.services;

import com.gjgs.gjgs.modules.exception.member.MemberNotFoundException;
import com.gjgs.gjgs.modules.exception.question.QuestionNotFoundException;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.lecture.services.authority.LectureDirectorAuthorityCheckable;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.question.dto.AnswerRequest;
import com.gjgs.gjgs.modules.question.dto.QuestionDetailResponse;
import com.gjgs.gjgs.modules.question.dto.QuestionRequest;
import com.gjgs.gjgs.modules.question.dto.QuestionResponse;
import com.gjgs.gjgs.modules.question.entity.Question;
import com.gjgs.gjgs.modules.question.repository.QuestionRepository;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionServiceImpl implements QuestionService {

    private final SecurityUtil securityUtil;
    private final QuestionRepository questionRepository;
    private final LectureDirectorAuthorityCheckable directorAuthorityCheckable;

    @Override
    public QuestionResponse createQuestion(QuestionRequest request) {
        Lecture lecture = directorAuthorityCheckable.findLecture(request.getLectureId());
        Member questioner = Member.from(securityUtil.getCurrentUsername().orElseThrow(() ->
                new MemberNotFoundException()
        ));
        questionRepository.save(Question.of(lecture, questioner, request));
        return QuestionResponse.createQuestion(lecture.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionDetailResponse getQuestion(Long questionId) {
        return questionRepository
                .findWithLectureQuestioner(questionId)
                .orElseThrow(() -> new QuestionNotFoundException());
    }

    @Override
    public QuestionResponse putAnswer(Long questionId, AnswerRequest request) {
        Question question = directorAuthorityCheckable.findQuestion(questionId);
        question.putAnswer(request);
        return QuestionResponse.createAnswer(question.getLecture().getId(), question.getId());
    }
}
