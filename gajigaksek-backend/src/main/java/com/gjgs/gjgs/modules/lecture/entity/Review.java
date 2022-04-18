package com.gjgs.gjgs.modules.lecture.entity;

import com.gjgs.gjgs.modules.lecture.dtos.review.CreateReviewRequest;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.utils.base.BaseEntity;
import com.gjgs.gjgs.modules.utils.vo.FileInfoVo;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity @Getter @NoArgsConstructor(access = PROTECTED) @AllArgsConstructor(access = PROTECTED) @Builder
@EqualsAndHashCode(of = "id", callSuper = false)
public class Review extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY) @Column(name = "REVIEW_ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "LECTURE_ID", nullable = false)
    private Lecture lecture;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String text;

    private String replyText;

    @Column(nullable = false)
    private int score;

    private String reviewImageFileUrl;

    public static Review of(Member member, FileInfoVo fileInfo, CreateReviewRequest request) {
        return Review.builder()
                .lecture(Lecture.of(request.getLectureId()))
                .member(member)
                .score(request.getScore())
                .text(request.getText())
                .reviewImageFileUrl(fileInfo.getFileUrl())
                .build();
    }

    public void reply(String replyText) {
        this.replyText = replyText;
    }
}
