package com.batch.redisbatch.service.impl;

import com.batch.redisbatch.domain.Member;
import com.batch.redisbatch.domain.MemberCoupon;
import com.batch.redisbatch.domain.Order;
import com.batch.redisbatch.domain.Reward;
import com.batch.redisbatch.repository.interfaces.MemberRepository;
import com.batch.redisbatch.repository.interfaces.RewardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CouponRewardDiscountImplTest {

    @Mock RewardRepository rewardRepository;
    @Mock MemberRepository memberRepository;
    @InjectMocks CouponRewardDiscountImpl couponRewardDiscount;

    @Test
    @DisplayName("리워드 환불, 쿠폰 환불, 회원의 보유 리워드 수정")
    void refund_reward_coupon() throws Exception {

        // given
        Member member = Member.builder().id(1L).build();
        MemberCoupon memberCoupon = MemberCoupon.builder().used(true).build();
        Reward reward = Reward.builder().amount(2000).build();
        Order order = Order.builder()
                .memberCoupon(memberCoupon)
                .reward(reward)
                .build();
        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));

        // when
        couponRewardDiscount.refund(order, member.getId());

        // then
        assertAll(
                () -> assertFalse(memberCoupon.isUsed()),
                () -> verify(rewardRepository).save(any()),
                () -> assertEquals(member.getTotalReward(), reward.getAmount())
        );
    }
}