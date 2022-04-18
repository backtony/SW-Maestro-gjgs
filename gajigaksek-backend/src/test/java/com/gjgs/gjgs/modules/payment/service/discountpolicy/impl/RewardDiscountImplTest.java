package com.gjgs.gjgs.modules.payment.service.discountpolicy.impl;

import com.gjgs.gjgs.modules.exception.payment.InvalidPriceException;
import com.gjgs.gjgs.modules.lecture.embedded.Price;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.lecture.entity.Schedule;
import com.gjgs.gjgs.modules.member.entity.Member;
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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RewardDiscountImplTest {

    @Mock RewardService rewardService;
    @Mock OrderRepository orderRepository;
    @InjectMocks RewardDiscountImpl rewardDiscount;

    @Test
    @DisplayName("리워드 적용 / 리워드 금액이 일치하지 않을 경우")
    void apply_pay_personal_test_invalid_reward_exception() throws Exception {

        // given
        Member member = createMember();
        Schedule schedule = createSchedule();
        PaymentRequest request = PaymentRequest.builder()
                .totalPrice(6000).rewardAmount(3000)
                .build();

        // when, then
        assertThrows(InvalidPriceException.class,
                () -> rewardDiscount.applyPayPersonal(member, schedule, request),
                "리워드 금액과 일치해야 한다.");
    }

    @Test
    @DisplayName("리워드 적용")
    void apply_pay_personal_test() throws Exception {

        // given
        Member member = createMember();
        Schedule schedule = createSchedule();
        PaymentRequest request = PaymentRequest.builder().totalPrice(7000).rewardAmount(3000).build();
        Reward reward = Reward.builder().id(1L).rewardType(RewardType.USE).member(member).amount(3000).build();
        stubbingRewardService(member, request, reward);
        stubbingOrder();

        // when
        rewardDiscount.applyPayPersonal(member, schedule, request);

        //then
        assertAll(
                () -> verify(orderRepository).save(any()),
                () -> verify(rewardService).useReward(member, request)
        );
    }

    private void stubbingOrder() {
        when(orderRepository.save(any())).thenReturn(Order.builder().id(1L).build());
    }

    private void stubbingRewardService(Member member, PaymentRequest request, Reward reward) {
        when(rewardService.useReward(member, request)).thenReturn(reward);
    }

    private Member createMember() {
        return Member.builder().id(1L)
                .totalReward(3000)
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