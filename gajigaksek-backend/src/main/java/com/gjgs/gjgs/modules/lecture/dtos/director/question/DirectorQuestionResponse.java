package com.gjgs.gjgs.modules.lecture.dtos.director.question;

import com.gjgs.gjgs.modules.question.enums.QuestionStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Getter @Setter @NoArgsConstructor(access = PRIVATE) @Builder
public class DirectorQuestionResponse {

    private LectureInfo lectureInfo;
    private QuestionInfo questionInfo;

    @QueryProjection
    public DirectorQuestionResponse(LectureInfo lectureInfo, QuestionInfo questionInfo) {
        this.lectureInfo = lectureInfo;
        this.questionInfo = questionInfo;
    }

    @Getter @Setter @NoArgsConstructor(access = PRIVATE) @Builder
    public static class LectureInfo {
        private Long id;
        private String title;
        private LocalDateTime lectureCreatedAt;

        @QueryProjection
        public LectureInfo(Long id, String title, LocalDateTime lectureCreatedAt) {
            this.id = id;
            this.title = title;
            this.lectureCreatedAt = lectureCreatedAt;
        }
    }

    @Getter @Setter @NoArgsConstructor(access = PRIVATE) @AllArgsConstructor @Builder
    public static class QuestionInfo {
        private Long id;
        private Long questionerId;
        private String questionerNickname;
        private String questionMainText;
        private String questionAnswerText;
        private String questionStatus;
        private LocalDateTime questionCreatedAt;

        @QueryProjection
        public QuestionInfo(Long id, Long questionerId, String questionerNickname, String questionMainText, String questionAnswerText, QuestionStatus questionStatus, LocalDateTime questionCreatedAt) {
            this.id = id;
            this.questionerId = questionerId;
            this.questionerNickname = questionerNickname;
            this.questionMainText = questionMainText;
            this.questionAnswerText = questionAnswerText;
            this.questionStatus = questionStatus.name();
            this.questionCreatedAt = questionCreatedAt;
        }
    }
}
