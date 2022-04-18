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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CouponDiscountImplTest {

    @Mock OrderRepository orderRepository;
    @InjectMocks CouponDiscountImpl couponDiscount;

    @Test
    @DisplayName("쿠폰 할인 적용하기 / 할인 금액이 맞지 않을 경우")
    void apply_pay_discount_test_invalid_price() throws Exception {

        // given
        Member member = createMember();
        Schedule schedule = createSchedule();
        PaymentRequest request = PaymentRequest.builder()
                .rewardAmount(0).memberCouponId(1L).totalPrice(8000).build();

        // when, then
        assertThrows(InvalidPriceException.class,
                () -> couponDiscount.applyPayPersonal(member, schedule, request),
                "쿠폰을 적용했을 때 금액이 일치해야 한다.");
    }

    @Test
    @DisplayName("쿠폰 할인 적용하기")
    void apply_pay_discount_test() throws Exception {

        // given
        Member member = createMember();
        Schedule schedule = createSchedule();
        PaymentRequest request = PaymentRequest.builder().rewardAmount(0).memberCouponId(1L).totalPrice(9000).build();
        when(orderRepository.save(any())).thenReturn(Order.builder().id(1L).build());

        // when
        couponDiscount.applyPayPersonal(member ,schedule, request);

        // then
        verify(orderRepository).save(any());
    }

    private Schedule createSchedule() {
        return Schedule.builder()
                .lecture(Lecture.builder()
                        .price(Price.builder().priceOne(10000).build())
                        .build())
                .build();
    }

    private Member createMember() {
        return Member.builder().id(1L)
                .coupons(new ArrayList<>(List.of(MemberCoupon.builder().id(1L)
                        .used(false).discountPrice(1000)
                        .build())))
                .build();
    }
}