package com.gjgs.gjgs.dummy;


import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.reward.entity.Reward;
import com.gjgs.gjgs.modules.reward.enums.RewardSaveType;
import com.gjgs.gjgs.modules.reward.repository.interfaces.RewardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("dev")
public class RewardDummy {

    private final RewardRepository rewardRepository;

    public Reward createRecommendTypeReward(Member member){
        Reward reward = Reward.createSaveReward(member, RewardSaveType.RECOMMEND);
        member.addReward(reward.getAmount());
        return rewardRepository.save(reward);
    }
}
