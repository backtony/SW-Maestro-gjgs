package com.gjgs.gjgs.modules.reward.service.interfaces;

import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.payment.dto.PaymentRequest;
import com.gjgs.gjgs.modules.reward.entity.Reward;

public interface RewardService {

    void SaveRecommendReward(String joinNickname,String recommendNickname);

    Reward useReward(Member member, PaymentRequest paymentRequest);

    void refundReward(Member member, Reward reward);
}
