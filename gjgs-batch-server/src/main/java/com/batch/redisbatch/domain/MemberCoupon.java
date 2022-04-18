package com.batch.redisbatch.domain;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberCoupon extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY) @Column(name = "MEMBER_COUPON_ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String serialNumber;

    @Column(nullable = false)
    private int discountPrice;

    @Column(nullable = false)
    private boolean used;

    public void refund() {
        this.used = false;
    }
}
