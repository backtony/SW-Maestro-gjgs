package com.gjgs.gjgs.modules.payment.service.discountpolicy.impl;

import com.gjgs.gjgs.modules.lecture.entity.Schedule;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.entity.MemberCoupon;
import com.gjgs.gjgs.modules.payment.dto.PaymentRequest;
import com.gjgs.gjgs.modules.payment.entity.Order;
import com.gjgs.gjgs.modules.payment.repository.OrderRepository;
import com.gjgs.gjgs.modules.payment.service.discountpolicy.DiscountPolicy;
import com.gjgs.gjgs.modules.payment.service.discountpolicy.DiscountType;
import com.gjgs.gjgs.modules.reward.entity.Reward;
import com.gjgs.gjgs.modules.reward.service.interfaces.RewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.gjgs.gjgs.modules.payment.service.discountpolicy.DiscountType.COUPON_REWARD;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponRewardDiscountImpl implements DiscountPolicy {

    private final OrderRepository orderRepository;
    private final RewardService rewardService;

    @Override
    public DiscountType getDiscountType() {
        return COUPON_REWARD;
    }

    @Override
    public Long applyPayPersonal(Member member, Schedule schedule, PaymentRequest paymentRequest) {
        MemberCoupon memberCoupon = member.getMemberCoupon(paymentRequest.getMemberCouponId());
        paymentRequest.setCouponDiscountPrice(memberCoupon.use());

        Reward reward = rewardService.useReward(member, paymentRequest);

        validApplyPrice(schedule.getLecturePrice(1), paymentRequest);

        Order createOrder = Order.ofPersonal(member, schedule, paymentRequest.getTotalDiscountPrice());
        createOrder.addMemberCoupon(memberCoupon);
        createOrder.addReward(reward);
        return orderRepository.save(createOrder).getId();
    }

    @Override
    public Long applyPayTeamMember(Member member, Order order, PaymentRequest paymentRequest) {
        MemberCoupon memberCoupon = member.getMemberCoupon(paymentRequest.getMemberCouponId());
        paymentRequest.setCouponDiscountPrice(memberCoupon.use());

        Reward reward = rewardService.useReward(member, paymentRequest);

        validApplyPrice(order.getOriginalPrice(), paymentRequest);

        order.applyPayment(paymentRequest);
        order.addMemberCoupon(memberCoupon);
        order.addReward(reward);
        return order.getId();
    }

    @Override
    public void refund(Member member, Order order, int refundPrice) {
        Reward reward = order.getReward();
        rewardService.refundReward(member, reward);
        MemberCoupon memberCoupon = order.getMemberCoupon();
        memberCoupon.refund();
        order.cancel();
    }
}
