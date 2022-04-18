package com.gjgs.gjgs.modules.question.dto;

import com.gjgs.gjgs.modules.question.enums.QuestionStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class QuestionDetailResponse {

    private Long questionId;
    private Long lectureId;
    private String questionStatus;
    private QuestionDetail questionDetail;
    private AnswerDetail answerDetail;

    @QueryProjection
    public QuestionDetailResponse(Long questionId, Long lectureId, QuestionStatus questionStatus, QuestionDetail questionDetail, AnswerDetail answerDetail) {
        this.questionId = questionId;
        this.lectureId = lectureId;
        this.questionStatus = questionStatus.name();
        this.questionDetail = questionDetail;
        this.answerDetail = answerDetail;
    }

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder
    public static class QuestionDetail {

        private String questionerNickname;
        private LocalDateTime questionDate;
        private String questionMainText;
        private String questionerProfileImageUrl;

        @QueryProjection
        public QuestionDetail(String questionerNickname, LocalDateTime questionDate, String questionMainText, String questionerProfileImageUrl) {
            this.questionerNickname = questionerNickname;
            this.questionDate = questionDate;
            this.questionMainText = questionMainText;
            this.questionerProfileImageUrl = questionerProfileImageUrl;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder
    public static class AnswerDetail {

        private String directorNickname;
        private String replyText;
        private String directorProfileImageUrl;

        @QueryProjection
        public AnswerDetail(String directorNickname, String replyText, String directorProfileImageUrl) {
            this.directorNickname = directorNickname;
            this.replyText = replyText;
            this.directorProfileImageUrl = directorProfileImageUrl;
        }
    }
}
