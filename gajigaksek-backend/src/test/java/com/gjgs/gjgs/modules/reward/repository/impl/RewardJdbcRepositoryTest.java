package com.gjgs.gjgs.modules.reward.repository.impl;


import com.gjgs.gjgs.config.repository.SetUpMemberRepository;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.reward.entity.Reward;
import com.gjgs.gjgs.modules.reward.enums.RewardSaveType;
import com.gjgs.gjgs.modules.reward.repository.interfaces.RewardJdbcRepository;
import com.gjgs.gjgs.modules.reward.repository.interfaces.RewardRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RewardJdbcRepositoryTest extends SetUpMemberRepository {

    @Autowired RewardJdbcRepository rewardJdbcRepository;
    @Autowired RewardRepository rewardRepository;

    @AfterEach
    void tearDown() throws Exception {
        rewardRepository.deleteAll();
    }

    @DisplayName("추천인 리워드 벌크 저장")
    @Test
    void insert_reward_list() throws Exception{

        //given
        Member m1 = anotherMembers.get(0);
        Member m2 = anotherMembers.get(1);
        Reward reward1 = Reward.createSaveReward(m1, RewardSaveType.RECOMMEND);
        Reward reward2 = Reward.createSaveReward(m2, RewardSaveType.RECOMMEND);

        //when
        rewardJdbcRepository.insertRewardList(List.of(reward1,reward2));
        flushAndClear();

        //then
        List<Reward> rewardList = rewardRepository.findAll();
        assertAll(
                () -> assertEquals(2,rewardList.size()),
                () -> assertEquals(RewardSaveType.RECOMMEND.getRewardType(),rewardList.get(0).getRewardType()),
                () -> assertEquals(RewardSaveType.RECOMMEND.getAmount(),rewardList.get(0).getAmount()),
                () -> assertEquals(RewardSaveType.RECOMMEND.getText(),rewardList.get(0).getText()),
                () -> assertEquals(m1,rewardList.get(0).getMember()),
                () -> assertNotNull(rewardList.get(0).getCreatedDate()),
                () -> assertNotNull(rewardList.get(0).getLastModifiedDate()),

                () -> assertEquals(RewardSaveType.RECOMMEND.getRewardType(),rewardList.get(1).getRewardType()),
                () -> assertEquals(RewardSaveType.RECOMMEND.getAmount(),rewardList.get(1).getAmount()),
                () -> assertEquals(RewardSaveType.RECOMMEND.getText(),rewardList.get(1).getText()),
                () -> assertEquals(m2,rewardList.get(1).getMember()),
                () -> assertNotNull(rewardList.get(1).getCreatedDate()),
                () -> assertNotNull(rewardList.get(1).getLastModifiedDate())
        );
    }
}
