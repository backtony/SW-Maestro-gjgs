package com.gjgs.gjgs.infra.config;

import com.gjgs.gjgs.infra.config.jwt.JwtAccessDeniedHandler;
import com.gjgs.gjgs.infra.config.jwt.JwtAuthenticationEntryPoint;
import com.gjgs.gjgs.infra.config.jwt.JwtSecurityConfig;
import com.gjgs.gjgs.modules.member.repository.interfaces.LogoutAccessTokenRedisRepository;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberRepository;
import com.gjgs.gjgs.modules.utils.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;


@RequiredArgsConstructor
@EnableWebSecurity // include configuration
@EnableGlobalMethodSecurity(prePostEnabled = true) // @preauthorize 애노테이션을 메서드 단위로 추가하기 위한 애노테이션
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;
    private final MemberRepository memberRepository;


    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/h2-console/**", "/favicon.ico");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // CSRF 설정 Disable
        http.csrf().disable()

                // exception handling 할 때 만든 클래스를 추가
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // h2-console 을 위한 설정을 추가
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                // 시큐리티는 기본적으로 세션을 사용하지만
                // 여기서는 세션을 사용하지 않기 때문에 세션 설정을 Stateless 로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 전부 허용 하고 필요한 부분은 preAuthoried로 해결
                .and()
                .authorizeRequests()
                .anyRequest().permitAll()

                // JwtFilter 추가 설정
                .and()
                .apply(new JwtSecurityConfig(jwtTokenUtil, logoutAccessTokenRedisRepository, memberRepository))

                // form login 해제
                .and()
                .httpBasic().disable()
        ;
    }
}