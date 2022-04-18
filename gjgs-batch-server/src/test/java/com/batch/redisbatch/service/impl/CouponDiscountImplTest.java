package com.batch.redisbatch.service.impl;

import com.batch.redisbatch.domain.MemberCoupon;
import com.batch.redisbatch.domain.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
public class CouponDiscountImplTest {

    @InjectMocks CouponDiscountImpl couponDiscount;

    @Test
    @DisplayName("쿠폰 환불")
    void refund_coupon() throws Exception {

        // given
        MemberCoupon memberCoupon = MemberCoupon.builder().used(true).build();
        Order order = Order.builder()
                .memberCoupon(memberCoupon)
                .build();

        // when
        couponDiscount.refund(order, 1L);

        // then
        assertFalse(memberCoupon.isUsed());
    }
}
