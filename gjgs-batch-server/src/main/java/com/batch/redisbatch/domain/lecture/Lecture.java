package com.batch.redisbatch.domain.lecture;

import com.batch.redisbatch.domain.BaseEntity;
import com.batch.redisbatch.domain.Category;
import com.batch.redisbatch.domain.Member;
import com.batch.redisbatch.domain.Zone;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

import static java.util.stream.Collectors.joining;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
public class Lecture extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "LECTURE_ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member director;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ZONE_ID")
    private Zone zone;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    @OneToMany(fetch = EAGER, mappedBy = "lecture", cascade = PERSIST)
    @BatchSize(size = 20)
    @Builder.Default
    private Set<Review> reviewList = new LinkedHashSet<>();

    @OneToMany(fetch = EAGER, mappedBy = "lecture")
    @BatchSize(size = 5)
    @Builder.Default
    private Set<FinishedProduct> finishedProductList = new LinkedHashSet<>();

    @Lob
    @Column(nullable = false)
    private String thumbnailImageFileUrl;

    @Lob
    private String thumbnailImageFileName;

    @Column(nullable = false)
    private String title;

    private int favoriteCount;

    @Embedded
    private Price price;

    @Embedded
    private Terms terms;

    @Enumerated(EnumType.STRING)
    private LectureStatus lectureStatus;

    private boolean finished;

    private String mainText;

    private int progressTime;

    private int clickCount;

    @Column(nullable = false)
    private String fullAddress;

    private double score;

    private boolean onlyGjgs;

    private int minParticipants;

    private int maxParticipants;

    private String rejectReason;

    public String getFinishedProductTexts() {
        return this.getFinishedProductList().stream()
                .map(FinishedProduct::getText)
                .collect(joining(" "));
    }
}
