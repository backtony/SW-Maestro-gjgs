package com.gjgs.gjgs.modules.question.dto;

import com.gjgs.gjgs.modules.member.dto.mypage.QuestionMainTextModifyRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QuestionMainTextModifyRequestTest {

    @DisplayName("mainTextForm 만들기")
    @Test
    void create_main_text_form() throws Exception {
        //given
        String mainText = "hello world";

        //when
        QuestionMainTextModifyRequest questionMainTextModifyRequest = QuestionMainTextModifyRequest.from(mainText);

        //then
        Assertions.assertEquals(mainText, questionMainTextModifyRequest.getMainText());
    }
}
