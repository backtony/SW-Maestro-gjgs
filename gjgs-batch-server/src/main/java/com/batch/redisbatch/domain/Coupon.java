package com.batch.redisbatch.domain;

import com.batch.redisbatch.domain.lecture.Lecture;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor(access = AccessLevel.PROTECTED) @Builder
@EqualsAndHashCode(of = "id", callSuper = false)
public class Coupon extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY) @Column(name = "COUPON_ID")
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "LECTURE_ID")
    private Lecture lecture;

    @Column(unique = true)
    private String serialNumber;

    @Column(nullable = false)
    private int discountPrice;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int remainCount;

    @Column(nullable = false)
    private int chargeCount;

    @Column(nullable = false)
    private boolean available;

    private LocalDateTime issueDate;

    private LocalDateTime closeDate;

    @Column(nullable = false)
    private int receivePeople;

    @Transient
    private static final String TITLE_TEMPLATE = " 원 할인 쿠폰";

}
