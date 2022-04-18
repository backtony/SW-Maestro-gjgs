package com.gjgs.gjgs.modules.question.dto;

import lombok.*;

import static com.gjgs.gjgs.modules.question.dto.QuestionResult.QUESTION;
import static com.gjgs.gjgs.modules.question.dto.QuestionResult.REPLY;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class QuestionResponse {

    private Long lectureId;
    private Long questionId;
    private String result;

    public static QuestionResponse createQuestion(Long lectureId) {
        return QuestionResponse.builder()
                .lectureId(lectureId)
                .result(QUESTION.getDescription())
                .build();
    }

    public static QuestionResponse createAnswer(Long lectureId, Long questionId) {
        return QuestionResponse.builder()
                .lectureId(lectureId)
                .questionId(questionId)
                .result(REPLY.getDescription())
                .build();
    }
}
