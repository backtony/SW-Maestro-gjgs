package com.gjgs.gjgs.infra.config.jwt;

import com.gjgs.gjgs.modules.exception.member.MemberNotFoundException;
import com.gjgs.gjgs.modules.exception.token.InvalidTokenException;
import com.gjgs.gjgs.modules.exception.token.TokenTypeException;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.repository.interfaces.LogoutAccessTokenRedisRepository;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberRepository;
import com.gjgs.gjgs.modules.utils.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final JwtTokenUtil jwtTokenUtil;
    private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;
    private final MemberRepository memberRepository;
    public static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwt = resolveToken(request);

        if (StringUtils.hasText(jwt)) {
            if (isValidToken(jwt)) {
                setAuthenticationToSecurityContextHolder(jwt);
            } else {
                throw new InvalidTokenException();
            }
        }

        filterChain.doFilter(request, response);

    }

    private void setAuthenticationToSecurityContextHolder(String jwt) {
        Member member = getCurrentUser(jwt);
        Authentication authentication = jwtTokenUtil.getAuthentication(jwt, member.getAuthority());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Member getCurrentUser(String jwt) {
        return memberRepository.findByUsername(jwtTokenUtil.getUsernameFromToken(jwt))
                .orElseThrow(() -> new MemberNotFoundException());
    }

    private boolean isValidToken(String jwt) {
        return jwtTokenUtil.validateToken(jwt) && logoutAccessTokenRedisRepository.existsById(jwt) == false;
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtTokenUtil.BEARER_TYPE)) {
            return bearerToken.substring(7);
        }

        if (StringUtils.hasText(bearerToken)) {
            throw new TokenTypeException();
        }
        return null;
    }
}