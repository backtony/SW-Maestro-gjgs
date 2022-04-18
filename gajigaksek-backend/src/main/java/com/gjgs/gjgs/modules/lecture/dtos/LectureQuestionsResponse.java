package com.gjgs.gjgs.modules.lecture.dtos;


import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class LectureQuestionsResponse {

    private Long questionId;
    private Long questionerId;
    private String questionerNickname;
    private String questionerProfileImageUrl;
    private LocalDateTime questionDate;
    private String questionText;
    private boolean answerComplete;

    @QueryProjection
    public LectureQuestionsResponse(Long questionId, Long questionerId, String questionerNickname, String questionerProfileImageUrl, LocalDateTime questionDate, String questionText, boolean answerComplete) {
        this.questionId = questionId;
        this.questionerId = questionerId;
        this.questionerNickname = questionerNickname;
        this.questionerProfileImageUrl = questionerProfileImageUrl;
        this.questionDate = questionDate;
        this.questionText = questionText;
        this.answerComplete = answerComplete;
    }
}
