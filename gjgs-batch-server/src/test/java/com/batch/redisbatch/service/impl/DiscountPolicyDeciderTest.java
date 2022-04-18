package com.batch.redisbatch.service.impl;

import com.batch.redisbatch.domain.MemberCoupon;
import com.batch.redisbatch.domain.Order;
import com.batch.redisbatch.domain.Reward;
import com.batch.redisbatch.service.policy.DiscountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiscountPolicyDeciderTest {

    @InjectMocks DiscountPolicyDecider discountPolicyDecider;
    @Mock CouponDiscountImpl couponDiscount;
    @Mock RewardDiscountImpl rewardDiscount;
    @Mock CouponRewardDiscountImpl couponRewardDiscount;

    @BeforeEach
    void setUp() throws Exception {
        discountPolicyDecider = new DiscountPolicyDecider(List.of(couponDiscount, rewardDiscount, couponRewardDiscount));
    }

    @Test
    @DisplayName("쿠폰과 리워드를 사용했을 경우")
    void get_coupon_reward_discount() throws Exception {

        // given
        Order order = Order.builder()
                .memberCoupon(MemberCoupon.builder().id(1L).build())
                .reward(Reward.builder().id(1L).build())
                .build();
        when(couponDiscount.getDiscountType()).thenReturn(DiscountType.COUPON);
        when(rewardDiscount.getDiscountType()).thenReturn(DiscountType.REWARD);
        when(couponRewardDiscount.getDiscountType()).thenReturn(DiscountType.COUPON_REWARD);

        // when
        discountPolicyDecider.refund(order, 1L);

        // then
        verify(couponRewardDiscount).refund(order, 1L);
    }

    @Test
    @DisplayName("쿠폰을 사용했을 경우")
    void get_coupon_discount() throws Exception {

        // given
        Order order = Order.builder()
                .memberCoupon(MemberCoupon.builder().id(1L).build())
                .build();
        when(couponDiscount.getDiscountType()).thenReturn(DiscountType.COUPON);

        // when
        discountPolicyDecider.refund(order, 1L);

        // then
        verify(couponDiscount).refund(order, 1L);
    }

    @Test
    @DisplayName("리워드를 사용했을 경우")
    void get_reward_discount() throws Exception {

        // given
        Order order = Order.builder()
                .reward(Reward.builder().id(1L).build())
                .build();
        when(couponDiscount.getDiscountType()).thenReturn(DiscountType.COUPON);
        when(rewardDiscount.getDiscountType()).thenReturn(DiscountType.REWARD);

        // when
        discountPolicyDecider.refund(order, 1L);

        // then
        verify(rewardDiscount).refund(order, 1L);
    }

    @Test
    @DisplayName("아무것도 사용하지 않았을 경우")
    void get_nothing_discount() throws Exception {

        // given
        Order order = Order.builder().build();

        // when
        discountPolicyDecider.refund(order, 1L);

        // then
        assertAll(
                () -> verify(rewardDiscount, never()).refund(order, 1L),
                () -> verify(couponDiscount, never()).refund(order, 1L),
                () -> verify(couponRewardDiscount, never()).refund(order, 1L)
        );
    }
}
