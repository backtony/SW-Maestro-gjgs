package com.gjgs.gjgs.modules.reward.aop;

import com.gjgs.gjgs.modules.category.services.interfaces.CategoryService;
import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.member.dto.login.SignUpRequest;
import com.gjgs.gjgs.modules.member.dto.login.TokenDto;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.repository.interfaces.LogoutAccessTokenRedisRepository;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberJdbcRepository;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberRepository;
import com.gjgs.gjgs.modules.member.repository.interfaces.RefreshTokenRedisRepository;
import com.gjgs.gjgs.modules.member.service.login.impl.KakaoService;
import com.gjgs.gjgs.modules.member.service.login.impl.LoginServiceImpl;
import com.gjgs.gjgs.modules.member.service.login.interfaces.LoginService;
import com.gjgs.gjgs.modules.reward.service.interfaces.RewardService;
import com.gjgs.gjgs.modules.utils.aop.RewardAspect;
import com.gjgs.gjgs.modules.utils.jwt.JwtTokenUtil;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import com.gjgs.gjgs.modules.zone.repositories.interfaces.ZoneRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RewardAspectTest {

    @Mock KakaoService kakaoService;
    @Mock MemberRepository memberRepository;
    @Mock JwtTokenUtil jwtTokenUtil;
    @Mock RefreshTokenRedisRepository refreshTokenRedisRepository;
    @Mock LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;
    @Mock ZoneRepository zoneRepository;
    @Mock CategoryService categoryService;
    @Mock MemberJdbcRepository memberJdbcRepository;
    @Mock SecurityUtil securityUtil;
    @InjectMocks LoginServiceImpl loginService;

    @Mock RewardService rewardService;
    @InjectMocks
    RewardAspect rewardAspect;

    @DisplayName("saveReward AOP 회원가입 리워드")
    @Test
    void save_reward() throws Exception{
        // given
        LoginService proxy = getAopProxy();

        SignUpRequest signUpRequest = MemberDummy.createSignupForm();
        Member member = MemberDummy.createTestMember();
        TokenDto tokenDto = TokenDto.of("Bearer", "AccessToken",
                "RefreshToken", 1L, 1L);

        // loginService
        when(jwtTokenUtil.generateTokenDto(ArgumentMatchers.any(Member.class))).thenReturn(tokenDto);
        when(memberRepository.save(any())).thenReturn(member);

        // when
        proxy.saveAndLogin(signUpRequest);

        // then
        verify(rewardService, Mockito.times(1)).SaveRecommendReward(any(),any());
    }

    private LoginService getAopProxy() {
        AspectJProxyFactory aspectJProxyFactory = new AspectJProxyFactory(loginService);
        aspectJProxyFactory.addAspect(rewardAspect);
        LoginService proxy = aspectJProxyFactory.getProxy();
        return proxy;
    }
}
