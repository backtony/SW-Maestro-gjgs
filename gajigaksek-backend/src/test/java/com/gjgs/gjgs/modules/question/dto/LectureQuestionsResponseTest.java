package com.gjgs.gjgs.modules.question.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.gjgs.gjgs.modules.question.dto.QuestionResult.QUESTION;
import static com.gjgs.gjgs.modules.question.dto.QuestionResult.REPLY;
import static org.junit.jupiter.api.Assertions.*;

class LectureQuestionsResponseTest {

    @Test
    @DisplayName("문의글 작성 완료")
    void create_question_test() throws Exception {

        // given
        Long lectureId = 1L;
        Long questionId = 2L;

        // when
        QuestionResponse res = QuestionResponse.createQuestion(lectureId);

        // then
        assertAll(
                () -> assertEquals(res.getLectureId(), lectureId),
                () -> assertNull(res.getQuestionId()),
                () -> assertEquals(res.getResult(), QUESTION.getDescription())
        );
    }

    @Test
    @DisplayName("문의글 답변 완료")
    void create_answer_test() throws Exception {

        // given
        Long lectureId = 1L;
        Long questionId = 2L;

        // when
        QuestionResponse res = QuestionResponse.createAnswer(lectureId, questionId);

        // then
        assertAll(
                () -> assertEquals(res.getLectureId(), lectureId),
                () -> assertEquals(res.getQuestionId(), questionId),
                () -> assertEquals(res.getResult(), REPLY.getDescription())
        );
    }


}