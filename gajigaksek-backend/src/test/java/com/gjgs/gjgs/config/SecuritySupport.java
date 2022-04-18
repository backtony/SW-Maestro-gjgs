package com.gjgs.gjgs.config;

import com.gjgs.gjgs.infra.config.jwt.JwtAccessDeniedHandler;
import com.gjgs.gjgs.infra.config.jwt.JwtAuthenticationEntryPoint;
import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.member.repository.interfaces.LogoutAccessTokenRedisRepository;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberRepository;
import com.gjgs.gjgs.modules.utils.jwt.JwtTokenUtil;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class SecuritySupport extends ControllerTest{

    @MockBean protected JwtTokenUtil jwtTokenUtil;
    @MockBean protected JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @MockBean protected JwtAccessDeniedHandler jwtAccessDeniedHandler;
    @MockBean protected LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;
    @MockBean protected MemberRepository memberRepository;


    protected void securityUserMockSetting() {
        Authentication authentication = MemberDummy.createAuthentication();
        when(jwtTokenUtil.validateToken(any())).thenReturn(true);
        when(logoutAccessTokenRedisRepository.existsById(any())).thenReturn(false);
        when(memberRepository.findByUsername(any())).thenReturn(Optional.of(MemberDummy.createTestMember()));
        when(jwtTokenUtil.getAuthentication(any(),any())).thenReturn(authentication);
    }

    protected void securityDirectorMockSetting() {
        Authentication authentication = MemberDummy.createDirectorAuthentication();
        when(jwtTokenUtil.validateToken(any())).thenReturn(true);
        when(logoutAccessTokenRedisRepository.existsById(any())).thenReturn(false);
        when(memberRepository.findByUsername(any())).thenReturn(Optional.of(MemberDummy.createTestMember()));
        when(jwtTokenUtil.getAuthentication(any(),any())).thenReturn(authentication);
    }

    protected void securityAdminMockSetting() {
        Authentication authentication = MemberDummy.createAdminAuthentication();
        when(jwtTokenUtil.validateToken(any())).thenReturn(true);
        when(logoutAccessTokenRedisRepository.existsById(any())).thenReturn(false);
        when(memberRepository.findByUsername(any())).thenReturn(Optional.of(MemberDummy.createTestMember()));
        when(jwtTokenUtil.getAuthentication(any(),any())).thenReturn(authentication);
    }
}
