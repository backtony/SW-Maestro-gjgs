package com.gjgs.gjgs.modules.member.dto;

import com.gjgs.gjgs.modules.dummy.RewardDummy;
import com.gjgs.gjgs.modules.member.dto.mypage.RewardDto;
import com.gjgs.gjgs.modules.member.dto.mypage.RewardResponse;
import com.gjgs.gjgs.modules.reward.enums.RewardSaveType;
import com.gjgs.gjgs.modules.utils.querydsl.RepositorySliceHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RewardResponseTest {

    @DisplayName("RewardDtoResponse 만들기")
    @Test
    void create_rewardDtoResponse() throws Exception{
        //given
        List<RewardDto> rewardDtoList = RewardDummy.createRewardDtoList();
        PageRequest pageRequest = PageRequest.of(0, 20);
        Slice<RewardDto> rewardDtoSlice = RepositorySliceHelper.toSlice(rewardDtoList, pageRequest);

        //when
        RewardResponse rewardResponse = RewardResponse.of(rewardDtoSlice, 3000);

        //then
        RewardDto rewardDto = rewardResponse.getRewardDtoSlice().getContent().get(0);
        assertAll(
                () -> assertEquals(3000, rewardResponse.getTotalReward()),
                () -> assertEquals(20, rewardResponse.getRewardDtoSlice().getNumberOfElements()),
                () -> assertEquals(RewardSaveType.RECOMMEND.getRewardType(), rewardDto.getRewardType()),
                () -> assertEquals(RewardSaveType.RECOMMEND.getText(), rewardDto.getText()),
                () -> assertEquals(RewardSaveType.RECOMMEND.getAmount(), rewardDto.getAmount())
        );
    }

}
