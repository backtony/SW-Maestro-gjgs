package com.batch.redisbatch.service.impl;

import com.batch.redisbatch.domain.MemberCoupon;
import com.batch.redisbatch.domain.Order;
import com.batch.redisbatch.service.policy.DiscountPolicy;
import com.batch.redisbatch.service.policy.DiscountType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.batch.redisbatch.service.policy.DiscountType.COUPON;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponDiscountImpl implements DiscountPolicy {

    @Override
    public DiscountType getDiscountType() {
        return COUPON;
    }

    @Override
    public void refund(Order order, Long memberId) {
        MemberCoupon memberCoupon = order.getMemberCoupon();
        memberCoupon.refund();
    }
}
