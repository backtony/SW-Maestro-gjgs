package com.gjgs.gjgs.modules.dummy;

import com.gjgs.gjgs.modules.member.dto.mypage.RewardDto;
import com.gjgs.gjgs.modules.reward.enums.RewardSaveType;
import com.gjgs.gjgs.modules.reward.enums.RewardType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RewardDummy {

    public static List<RewardDto> createRewardDtoList() {
        List<RewardDto> resList = new ArrayList<>();

        for (Long i = 1L; i <= 20; i++) {
            resList.add(RewardDto.builder()
                    .rewardId(i)
                    .amount(3000)
                    .text(RewardSaveType.RECOMMEND.getText())
                    .rewardType(RewardType.SAVE)
                    .createdDate(LocalDateTime.now())
                    .build());
        }
        return resList;
    }


}
