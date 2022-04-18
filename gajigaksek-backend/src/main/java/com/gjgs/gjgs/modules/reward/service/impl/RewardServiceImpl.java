package com.gjgs.gjgs.modules.reward.service.impl;

import com.gjgs.gjgs.modules.exception.member.MemberNotFoundException;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberRepository;
import com.gjgs.gjgs.modules.payment.dto.PaymentRequest;
import com.gjgs.gjgs.modules.reward.entity.Reward;
import com.gjgs.gjgs.modules.reward.enums.RewardSaveType;
import com.gjgs.gjgs.modules.reward.repository.interfaces.RewardJdbcRepository;
import com.gjgs.gjgs.modules.reward.repository.interfaces.RewardRepository;
import com.gjgs.gjgs.modules.reward.service.interfaces.RewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RewardServiceImpl implements RewardService {

    private final RewardJdbcRepository rewardJdbcRepository;
    private final MemberRepository memberRepository;
    private final RewardRepository rewardRepository;

    @Override
    public void SaveRecommendReward(String joinNickname, String recommendNickname) {

        Member joinMember = memberRepository.findByNickname(joinNickname)
                .orElseThrow(() -> new MemberNotFoundException());
        Member recommendMember = memberRepository.findByNickname(recommendNickname)
                .orElseThrow(() -> new MemberNotFoundException());

        Reward joinReward = Reward.createSaveReward(joinMember, RewardSaveType.RECOMMEND);
        Reward recommendReward = Reward.createSaveReward(recommendMember, RewardSaveType.RECOMMEND);
        rewardJdbcRepository.insertRewardList(List.of(joinReward,recommendReward));

        joinMember.addReward(RewardSaveType.RECOMMEND.getAmount());
        recommendMember.addReward(RewardSaveType.RECOMMEND.getAmount());

    }

    @Override
    public Reward useReward(Member member, PaymentRequest paymentRequest) {
        int useRewardAmount = paymentRequest.getRewardAmount();
        member.useReward(useRewardAmount);
        return rewardRepository.save(Reward.createUseReward(member, useRewardAmount));
    }

    @Override
    public void refundReward(Member member, Reward reward) {
        int rewardAmount = reward.getAmount();
        member.addReward(rewardAmount);
        rewardRepository.save(Reward.createCancelReward(member, rewardAmount));
    }
}
