package com.batch.redisbatch.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class MemberCouponTest {

    @Test
    @DisplayName("쿠폰 환불처리")
    void refund_member_coupon() throws Exception {

        // given
        MemberCoupon memberCoupon = MemberCoupon.builder().used(true).build();

        // when
        memberCoupon.refund();

        // then
        assertFalse(memberCoupon.isUsed());
    }
}