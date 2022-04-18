package com.batch.redisbatch.service.impl;


import com.batch.redisbatch.domain.Order;
import com.batch.redisbatch.service.policy.DiscountPolicy;
import com.batch.redisbatch.service.policy.DiscountType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.batch.redisbatch.service.policy.DiscountType.*;

@Component
@RequiredArgsConstructor
public class DiscountPolicyDecider {

    private final List<DiscountPolicy> discountPolicyList;

    public void refund(Order order, Long memberId) {
        DiscountType refundType = getRefundType(order);
        if (!refundType.equals(NOTHING)) {
            discountPolicyList.stream()
                    .filter(discountPolicy -> discountPolicy.getDiscountType().equals(refundType))
                    .findFirst()
                    .get()
                    .refund(order, memberId);
        }
    }

    private DiscountType getRefundType(Order order) {
        if (order.getReward() != null && order.getMemberCoupon() != null) {
            return COUPON_REWARD;
        } else if (order.getMemberCoupon() != null && order.getReward() == null) {
            return COUPON;
        } else if (order.getMemberCoupon() == null && order.getReward() != null) {
            return REWARD;
        } else {
            return NOTHING;
        }
    }
}
