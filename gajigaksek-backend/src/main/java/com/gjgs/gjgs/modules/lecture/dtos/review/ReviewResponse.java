package com.gjgs.gjgs.modules.lecture.dtos.review;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static lombok.AccessLevel.PRIVATE;

@Getter @Setter @NoArgsConstructor(access = PRIVATE) @Builder
public class ReviewResponse {

    private Long reviewId;
    private String reviewImageFileUrl;
    private String text;
    private String replyText;
    private int score;
    private String nickname;
    private String profileImageFileUrl;

    @QueryProjection
    public ReviewResponse(Long reviewId, String reviewImageFileUrl, String text, String replyText, int score, String nickname, String profileImageFileUrl) {
        this.reviewId = reviewId;
        this.reviewImageFileUrl = reviewImageFileUrl;
        this.text = text;
        this.replyText = replyText;
        this.score = score;
        this.nickname = nickname;
        this.profileImageFileUrl = profileImageFileUrl;
    }
}
