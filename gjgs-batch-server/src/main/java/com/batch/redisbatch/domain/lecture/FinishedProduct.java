package com.batch.redisbatch.domain.lecture;

import com.batch.redisbatch.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
public class FinishedProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY) @Column(name = "FINISHED_PRODUCT_ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "LECTURE_ID")
    private Lecture lecture;
    private int orders;
    private String finishedProductImageName;
    private String finishedProductImageUrl;
    private String text;
}
