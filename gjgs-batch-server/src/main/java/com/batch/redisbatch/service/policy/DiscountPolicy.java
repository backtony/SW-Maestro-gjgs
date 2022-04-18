package com.batch.redisbatch.service.policy;

import com.batch.redisbatch.domain.Order;

public interface DiscountPolicy {

    DiscountType getDiscountType();

    void refund(Order order, Long memberId);
}
