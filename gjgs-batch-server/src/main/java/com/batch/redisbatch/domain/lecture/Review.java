package com.batch.redisbatch.domain.lecture;

import com.batch.redisbatch.domain.BaseEntity;
import com.batch.redisbatch.domain.Member;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity @Getter @NoArgsConstructor(access = PROTECTED) @AllArgsConstructor(access = PROTECTED) @Builder
@EqualsAndHashCode(of = "id", callSuper = false)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY) @Column(name = "REVIEW_ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "LECTURE_ID")
    private Lecture lecture;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Column(nullable = false)
    private String text;

    private String replyText;

    @Column(nullable = false)
    private int score;

    private String reviewImageFileUrl;
}
