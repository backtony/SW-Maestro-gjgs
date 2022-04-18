package com.gjgs.gjgs.modules.payment.service.discountpolicy.impl;

import com.gjgs.gjgs.modules.lecture.entity.Schedule;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.payment.dto.PaymentRequest;
import com.gjgs.gjgs.modules.payment.entity.Order;
import com.gjgs.gjgs.modules.payment.repository.OrderRepository;
import com.gjgs.gjgs.modules.payment.service.discountpolicy.DiscountPolicy;
import com.gjgs.gjgs.modules.payment.service.discountpolicy.DiscountType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.gjgs.gjgs.modules.payment.service.discountpolicy.DiscountType.NOTHING;

@Service
@RequiredArgsConstructor
@Transactional
public class NothingDiscountImpl implements DiscountPolicy {

    private final OrderRepository orderRepository;

    @Override
    public DiscountType getDiscountType() {
        return NOTHING;
    }

    @Override
    public Long applyPayPersonal(Member member, Schedule schedule, PaymentRequest paymentRequest) {
        validApplyPrice(schedule.getLecturePrice(1), paymentRequest);
        Order savedOrder = orderRepository.save(Order.ofPersonal(member, schedule, 0));
        return savedOrder.getId();
    }

    @Override
    public Long applyPayTeamMember(Member member, Order order, PaymentRequest paymentRequest) {
        validApplyPrice(order.getOriginalPrice(), paymentRequest);
        order.applyPayment(paymentRequest);
        return order.getId();
    }

    @Override
    public void refund(Member member, Order order, int refundPrice) {
        order.cancel();
    }
}
