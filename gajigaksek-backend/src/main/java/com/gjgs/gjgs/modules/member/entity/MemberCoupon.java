package com.gjgs.gjgs.modules.member.entity;

import com.gjgs.gjgs.modules.exception.coupon.AlreadyUsedCouponException;
import com.gjgs.gjgs.modules.exception.coupon.NotUsedCouponException;
import com.gjgs.gjgs.modules.utils.base.BaseEntity;
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

    public static MemberCoupon of(Member couponReceiver, int discountPrice, String serialNumber) {
        return MemberCoupon.builder()
                .member(couponReceiver)
                .used(false)
                .discountPrice(discountPrice)
                .serialNumber(serialNumber)
                .build();
    }

    public int use() {
        checkUsed();
        this.used = true;
        return this.getDiscountPrice();
    }

    private void checkUsed() {
        if (isUsed()) {
            throw new AlreadyUsedCouponException();
        }
    }

    public void refund() {
        checkNotUsed();
        this.used = false;
    }

    private void checkNotUsed() {
        if (!isUsed()) {
            throw new NotUsedCouponException();
        }
    }
}
