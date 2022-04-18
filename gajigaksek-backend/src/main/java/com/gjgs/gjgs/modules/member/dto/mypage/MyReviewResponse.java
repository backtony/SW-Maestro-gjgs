package com.gjgs.gjgs.modules.member.dto.mypage;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static lombok.AccessLevel.PRIVATE;

@Getter @Setter @NoArgsConstructor(access = PRIVATE) @Builder
public class MyReviewResponse {

    private Long lectureId;
    private String lectureTitle;
    private String reviewImageFileUrl;
    private String text;
    private String replyText;
    private int score;

    @QueryProjection
    public MyReviewResponse(Long lectureId, String lectureTitle, String reviewImageFileUrl, String text, String replyText, int score) {
        this.lectureId = lectureId;
        this.lectureTitle = lectureTitle;
        this.reviewImageFileUrl = reviewImageFileUrl;
        this.text = text;
        this.replyText = replyText;
        this.score = score;
    }
}
