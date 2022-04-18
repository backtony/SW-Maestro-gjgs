package com.gjgs.gjgs.modules.question.entity;


import com.gjgs.gjgs.modules.dummy.QuestionDummy;
import com.gjgs.gjgs.modules.question.dto.AnswerRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.gjgs.gjgs.modules.question.enums.QuestionStatus.COMPLETE;
import static com.gjgs.gjgs.modules.question.enums.QuestionStatus.WAIT;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class QuestionTest {

    @DisplayName("문의 수정하기")
    @Test
    void change_main_text() throws Exception {
        //given
        Question question = QuestionDummy.createOnlyMainTextQuestion();
        String mainText = "change Main Text";

        // when
        question.changeMainText(mainText);

        //then
        assertEquals(mainText, question.getMainText());
    }

    @Test
    @DisplayName("답변 달기")
    void put_answer_test() throws Exception {

        // given
        AnswerRequest request = AnswerRequest.builder()
                .replyText("답변테스트")
                .build();
        Question question = Question.builder()
                .mainText("테스트").questionStatus(WAIT)
                .build();

        // when
        question.putAnswer(request);

        // then
        assertAll(
                () -> assertEquals(question.getQuestionStatus(), COMPLETE),
                () -> assertEquals(question.getReplyText(), request.getReplyText())
        );
    }
}
