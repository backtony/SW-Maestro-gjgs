package com.gjgs.gjgs.modules.payment.service.discountpolicy.impl;

import com.gjgs.gjgs.modules.lecture.embedded.Price;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.lecture.entity.Schedule;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.payment.dto.PaymentRequest;
import com.gjgs.gjgs.modules.payment.entity.Order;
import com.gjgs.gjgs.modules.payment.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NothingDiscountImplTest {

    @Mock OrderRepository orderRepository;
    @InjectMocks NothingDiscountImpl nothingDiscount;

    @Test
    @DisplayName("할인이 적용되지 않은 금액")
    void apply_pay_discount_test() throws Exception {

        // given
        Member member = Member.builder().id(1L).build();
        Schedule schedule = Schedule.builder().id(1L)
                .lecture(Lecture.builder()
                        .price(Price.builder().priceOne(10000).build())
                        .build())
                .build();
        PaymentRequest request = PaymentRequest.builder().rewardAmount(0).totalPrice(10000).build();
        when(orderRepository.save(any())).thenReturn(Order.builder().id(1L).build());

        // when
        nothingDiscount.applyPayPersonal(member, schedule, request);

        // then
        verify(orderRepository).save(any());
    }
}