package com.gjgs.gjgs.modules.utils.aop;

import com.gjgs.gjgs.modules.member.dto.login.SignUpRequest;
import com.gjgs.gjgs.modules.reward.service.interfaces.RewardService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Aspect
@Component
@RequiredArgsConstructor
public class RewardAspect {

    private final RewardService rewardService;

    @AfterReturning("@annotation(saveReward) && args(signUpRequest)")
    public void saveReward(JoinPoint joinPoint, SignUpRequest signUpRequest){
        if (StringUtils.hasText(signUpRequest.getRecommendNickname())){
            rewardService.SaveRecommendReward(signUpRequest.getNickname(), signUpRequest.getRecommendNickname());
        }
    }

}
