package com.gjgs.gjgs.modules.payment.service.discountpolicy.impl;

import com.gjgs.gjgs.modules.lecture.entity.Schedule;
import com.gjgs.gjgs.modules.member.entity.Member;
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

import static com.gjgs.gjgs.modules.payment.service.discountpolicy.DiscountType.REWARD;

@Service
@RequiredArgsConstructor
@Transactional
public class RewardDiscountImpl implements DiscountPolicy {

    private final RewardService rewardService;
    private final OrderRepository orderRepository;

    @Override
    public DiscountType getDiscountType() {
        return REWARD;
    }

    @Override
    public Long applyPayPersonal(Member member, Schedule schedule, PaymentRequest paymentRequest) {
        Reward reward = rewardService.useReward(member, paymentRequest);

        validApplyPrice(schedule.getLecturePrice(1), paymentRequest);

        Order createOrder = Order.ofPersonal(member, schedule, paymentRequest.getTotalDiscountPrice());
        createOrder.addReward(reward);
        return orderRepository.save(createOrder).getId();
    }

    @Override
    public Long applyPayTeamMember(Member member, Order order, PaymentRequest paymentRequest) {
        Reward reward = rewardService.useReward(member, paymentRequest);

        validApplyPrice(order.getOriginalPrice(), paymentRequest);

        order.applyPayment(paymentRequest);
        order.addReward(reward);
        return order.getId();
    }

    @Override
    public void refund(Member member, Order order, int refundPrice) {
        Reward reward = order.getReward();
        order.cancel();
        rewardService.refundReward(member, reward);
    }
}
