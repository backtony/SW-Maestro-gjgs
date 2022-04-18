package com.gjgs.gjgs.modules.lecture.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReviewTest {

    @Test
    @DisplayName("리뷰 답글 작성하기")
    void reply_test() throws Exception {

        // given
        Review review = Review.builder().build();
        String replyText = "replyText";

        // when
        review.reply(replyText);

        // then
        assertEquals(review.getReplyText(), replyText);
    }
}