package com.gjgs.gjgs.modules.payment.service.discountpolicy.impl;

import com.gjgs.gjgs.modules.lecture.entity.Schedule;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.entity.MemberCoupon;
import com.gjgs.gjgs.modules.payment.dto.PaymentRequest;
import com.gjgs.gjgs.modules.payment.entity.Order;
import com.gjgs.gjgs.modules.payment.repository.OrderRepository;
import com.gjgs.gjgs.modules.payment.service.discountpolicy.DiscountPolicy;
import com.gjgs.gjgs.modules.payment.service.discountpolicy.DiscountType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.gjgs.gjgs.modules.payment.service.discountpolicy.DiscountType.COUPON;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponDiscountImpl implements DiscountPolicy {

    private final OrderRepository orderRepository;

    @Override
    public DiscountType getDiscountType() {
        return COUPON;
    }

    @Override
    public Long applyPayPersonal(Member member, Schedule schedule, PaymentRequest paymentRequest) {
        MemberCoupon memberCoupon = member.getMemberCoupon(paymentRequest.getMemberCouponId());
        paymentRequest.setCouponDiscountPrice(memberCoupon.use());

        validApplyPrice(schedule.getLecturePrice(1), paymentRequest);

        Order createOrder = Order.ofPersonal(member, schedule, paymentRequest.getTotalDiscountPrice());
        createOrder.addMemberCoupon(memberCoupon);
        return orderRepository.save(createOrder).getId();
    }

    @Override
    public Long applyPayTeamMember(Member member, Order order, PaymentRequest paymentRequest) {
        MemberCoupon memberCoupon = member.getMemberCoupon(paymentRequest.getMemberCouponId());
        paymentRequest.setCouponDiscountPrice(memberCoupon.use());

        validApplyPrice(order.getOriginalPrice(), paymentRequest);

        order.applyPayment(paymentRequest);
        order.addMemberCoupon(memberCoupon);
        return order.getId();
    }

    @Override
    public void refund(Member member, Order order, int refundPrice) {
        MemberCoupon memberCoupon = order.getMemberCoupon();
        order.cancel();
        memberCoupon.refund();
    }
}
