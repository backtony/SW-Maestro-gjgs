package com.gjgs.gjgs.modules.reward.repository.interfaces;

import com.gjgs.gjgs.modules.reward.entity.Reward;

import java.util.List;

public interface RewardJdbcRepository {

    void insertRewardList(List<Reward> rewardList);
}
