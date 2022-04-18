package com.gjgs.gjgs.modules.question.services;

import com.gjgs.gjgs.modules.question.dto.AnswerRequest;
import com.gjgs.gjgs.modules.question.dto.QuestionDetailResponse;
import com.gjgs.gjgs.modules.question.dto.QuestionRequest;
import com.gjgs.gjgs.modules.question.dto.QuestionResponse;

public interface QuestionService {

    QuestionResponse createQuestion(QuestionRequest request);

    QuestionDetailResponse getQuestion(Long questionId);

    QuestionResponse putAnswer(Long questionId, AnswerRequest request);
}
