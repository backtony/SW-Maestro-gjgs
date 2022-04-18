package com.gjgs.gjgs.modules.payment.service.discountpolicy.impl;

import com.gjgs.gjgs.modules.exception.payment.InvalidPriceException;
import com.gjgs.gjgs.modules.lecture.embedded.Price;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.lecture.entity.Schedule;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.entity.MemberCoupon;
import com.gjgs.gjgs.modules.payment.dto.PaymentRequest;
import com.gjgs.gjgs.modules.payment.entity.Order;
import com.gjgs.gjgs.modules.payment.repository.OrderRepository;
import com.gjgs.gjgs.modules.reward.entity.Reward;
import com.gjgs.gjgs.modules.reward.enums.RewardType;
import com.gjgs.gjgs.modules.reward.service.interfaces.RewardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CouponRewardDiscountImplTest {

    @Mock RewardService rewardService;
    @Mock OrderRepository orderRepository;
    @InjectMocks CouponRewardDiscountImpl couponRewardDiscount;

    @Test
    @DisplayName("쿠폰과 리워드 동시 적용 / 금액 불일치 일 때, 예외 발생")
    void apply_pay_personal_test_invalid_price_exception() throws Exception {

        // given
        Member member = createMember();
        Schedule schedule = createSchedule();
        PaymentRequest request = PaymentRequest.builder().rewardAmount(4000).totalPrice(7000).memberCouponId(1L).build();

        // when, then
        assertThrows(InvalidPriceException.class,
                () -> couponRewardDiscount.applyPayPersonal(member ,schedule, request),
                "금액이 일치하지 않을 경우 예외 발생");
    }

    @Test
    @DisplayName("쿠폰과 리워드 동시 적용")
    void apply_pay_personal_test() throws Exception {

        // given
        Member member = createMember();
        Schedule schedule = createSchedule();
        PaymentRequest request = PaymentRequest.builder().rewardAmount(3000)
                .totalPrice(6000).memberCouponId(1L).build();
        stubbingReward(member, request);
        stubbingOrder();

        // when
        couponRewardDiscount.applyPayPersonal(member, schedule, request);

        // then
        assertAll(
                () -> verify(rewardService).useReward(member, request),
                () -> verify(orderRepository).save(any()),
                () -> assertTrue(member.getMemberCoupon(request.getMemberCouponId()).isUsed())
        );
    }

    private void stubbingOrder() {
        when(orderRepository.save(any())).thenReturn(Order.builder().id(1L).build());
    }

    private void stubbingReward(Member member, PaymentRequest request) {
        when(rewardService.useReward(member, request))
                .thenReturn(Reward.builder().rewardType(RewardType.USE).member(member).amount(request.getRewardAmount()).build());
    }

    private Member createMember() {
        return Member.builder().id(1L)
                .totalReward(3000)
                .coupons(new ArrayList<>(List.of(MemberCoupon.builder().id(1L)
                        .used(false).discountPrice(1000)
                        .build())))
                .build();
    }

    private Schedule createSchedule() {
        return Schedule.builder()
                .lecture(Lecture.builder()
                        .price(Price.builder().priceOne(10000).build())
                        .build())
                .build();
    }
}