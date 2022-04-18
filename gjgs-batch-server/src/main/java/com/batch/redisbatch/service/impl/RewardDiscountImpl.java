package com.batch.redisbatch.service.impl;

import com.batch.redisbatch.domain.Member;
import com.batch.redisbatch.domain.Order;
import com.batch.redisbatch.domain.Reward;
import com.batch.redisbatch.repository.interfaces.MemberRepository;
import com.batch.redisbatch.repository.interfaces.RewardRepository;
import com.batch.redisbatch.service.policy.DiscountPolicy;
import com.batch.redisbatch.service.policy.DiscountType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RewardDiscountImpl implements DiscountPolicy {

    private final RewardRepository rewardRepository;
    private final MemberRepository memberRepository;

    @Override
    public DiscountType getDiscountType() {
        return DiscountType.REWARD;
    }

    @Override
    public void refund(Order order, Long memberId) {
        Reward reward = order.getReward();
        Optional<Member> member = memberRepository.findById(memberId);
        member.ifPresentOrElse(m -> m.addReward(reward.getAmount()),
                () -> log.error("존재하지 않는 회원입니다."));
        rewardRepository.save(Reward.ofPaymentCancel(reward.getAmount()));
    }
}
