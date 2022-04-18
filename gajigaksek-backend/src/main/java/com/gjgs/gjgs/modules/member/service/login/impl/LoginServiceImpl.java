package com.gjgs.gjgs.modules.member.service.login.impl;

import com.gjgs.gjgs.modules.exception.member.InvalidAuthorityException;
import com.gjgs.gjgs.modules.exception.member.MemberNotFoundException;
import com.gjgs.gjgs.modules.exception.token.InvalidTokenException;
import com.gjgs.gjgs.modules.exception.token.NoRefreshTokenException;
import com.gjgs.gjgs.modules.exception.token.TokenTypeException;
import com.gjgs.gjgs.modules.member.dto.login.KakaoProfile;
import com.gjgs.gjgs.modules.member.dto.login.LoginResponse;
import com.gjgs.gjgs.modules.member.dto.login.SignUpRequest;
import com.gjgs.gjgs.modules.member.dto.login.TokenDto;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.enums.Authority;
import com.gjgs.gjgs.modules.member.redis.LogoutAccessToken;
import com.gjgs.gjgs.modules.member.redis.RefreshToken;
import com.gjgs.gjgs.modules.member.repository.interfaces.LogoutAccessTokenRedisRepository;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberJdbcRepository;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberRepository;
import com.gjgs.gjgs.modules.member.repository.interfaces.RefreshTokenRedisRepository;
import com.gjgs.gjgs.modules.member.service.login.interfaces.LoginService;
import com.gjgs.gjgs.modules.utils.aop.saveReward;
import com.gjgs.gjgs.modules.utils.jwt.JwtTokenUtil;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private static final String ROLE = "ROLE_";

    private final MemberRepository memberRepository;
    private final MemberJdbcRepository memberJdbcRepository;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;
    private final KakaoService kakaoService;
    private final JwtTokenUtil jwtTokenUtil;
    private final SecurityUtil securityUtil;


    @Override
    public LoginResponse login(String accessToken, String fcmToken) {
        KakaoProfile kakaoProfile = kakaoService.getKakaoProfile(accessToken);
        Optional<Member> member = memberRepository.findByUsername(String.valueOf(kakaoProfile.getId()));

        if (member.isEmpty()) {
            return LoginResponse.from(kakaoProfile);
        }

        Member currentUser = member.get();
        currentUser.changeFcmToken(fcmToken);
        TokenDto tokenDto = jwtTokenUtil.generateTokenDto(currentUser);
        createRedisRefreshTokenAndSave(currentUser.getUsername(), tokenDto.getRefreshToken());
        return LoginResponse.of(currentUser.getId(), tokenDto, kakaoProfile);
    }

    @Override
    public LoginResponse webLogin(String accessToken, String authority) {
        KakaoProfile kakaoProfile = kakaoService.getKakaoProfile(accessToken);
        Member currentUser = memberRepository.findByUsername(String.valueOf(kakaoProfile.getId()))
                .orElseThrow(() -> new MemberNotFoundException());
        checkCurrentUserAuthority(authority, currentUser);
        TokenDto tokenDto = jwtTokenUtil.generateTokenDto(currentUser);
        createRedisRefreshTokenAndSave(currentUser.getUsername(), tokenDto.getRefreshToken());
        return LoginResponse.of(currentUser.getId(), tokenDto, kakaoProfile);
    }

    private void checkCurrentUserAuthority(String authority, Member member) {
        Authority auth = getAuthority(authority);

        if (isNotMatchAuthority(member, auth)) {
            throw new InvalidAuthorityException();
        }
    }

    private Authority getAuthority(String authority) {
        String role = ROLE + authority.toUpperCase();
        Authority auth = Authority.valueOf(role);
        return auth;
    }

    private boolean isNotMatchAuthority(Member member, Authority auth) {
        return !Arrays.stream(Authority.values()).anyMatch(auth::equals) || !member.getAuthority().equals(auth);
    }


    @Override
    @saveReward
    public LoginResponse saveAndLogin(SignUpRequest signUpRequest) {
        Member member = memberRepository.save(Member.from(signUpRequest));
        memberJdbcRepository.insertMemberCategoryList(member.getId(), signUpRequest.getCategoryIdList());
        TokenDto tokenDto = jwtTokenUtil.generateTokenDto(member);
        createRedisRefreshTokenAndSave(member.getUsername(), tokenDto.getRefreshToken());
        return LoginResponse.of(member.getId(), tokenDto, signUpRequest.getId(), signUpRequest.getImageFileUrl());
    }

    /**
     * refreshToken이 7일 이하로 남았을 경우 Refresh도 재발급
     * refreshToken이 7일 초과로 남았을 경우 Access만 재발급
     */
    @Override
    public TokenDto reissue(String refreshToken) {
        String token = resolveToken(refreshToken);
        String username = getUsername();
        Authentication authentication = getAuthentication(token);
        String refreshFromRedis = refreshTokenRedisRepository.findById(username)
                .orElseThrow(() -> new NoRefreshTokenException())
                .getRefreshToken();

        checkRefreshTokenValidation(token, refreshFromRedis);

        if (shouldReissueRefreshToken(refreshFromRedis)) {
            TokenDto tokenDto = jwtTokenUtil.generateTokenDto(authentication);
            createRedisRefreshTokenAndSave(username, tokenDto.getRefreshToken());
            return tokenDto;
        } else {
            String newAccessToken = jwtTokenUtil.generateAccessToken(authentication);
            return TokenDto.of(
                    JwtTokenUtil.BEARER_TYPE,
                    newAccessToken,
                    refreshFromRedis,
                    jwtTokenUtil.getRemainingMilliSeconds(newAccessToken),
                    jwtTokenUtil.getRemainingMilliSeconds(refreshFromRedis));
        }


    }

    private void checkRefreshTokenValidation(String token, String refreshFromRedis) {
        if (!token.equals(refreshFromRedis)) {
            throw new InvalidTokenException();
        }
    }


    private boolean shouldReissueRefreshToken(String refreshFromRedis) {
        return jwtTokenUtil.getRemainingMilliSeconds(refreshFromRedis) < JwtTokenUtil.REFRESH_TOKEN_REISSUE_TIME;
    }

    private Authentication getAuthentication(String token) {
        Authority authority = securityUtil.getAuthority()
                .orElseThrow(() -> new MemberNotFoundException());
        Authentication authentication = jwtTokenUtil.getAuthentication(token, authority);
        return authentication;
    }


    @Override
    public void logout(String accessToken, String refreshToken) {
        String token = resolveToken(accessToken);
        String username = getUsername();

        memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberNotFoundException())
                .clearFcmToken();
        refreshTokenRedisRepository.deleteById(username);

        // redis에 accessToken을 남은 시간동안 삽입 -> 해당 토큰으로 로그인 막기 위함
        logoutAccessTokenRedisRepository
                .save(LogoutAccessToken.of(token, username, jwtTokenUtil.getRemainingMilliSeconds(token)));
    }

    private String resolveToken(String token) {
        if (StringUtils.hasText(token) && token.startsWith(JwtTokenUtil.BEARER_TYPE)) {
            return token.substring(7);
        }
        throw new TokenTypeException();
    }

    private String getUsername() {
        return securityUtil.getCurrentUsername()
                .orElseThrow(() -> new MemberNotFoundException());
    }

    private void createRedisRefreshTokenAndSave(String username, String refreshToken) {
        RefreshToken redisRefreshToken = RefreshToken.of(username, refreshToken
                , jwtTokenUtil.getRemainingMilliSeconds(refreshToken));
        refreshTokenRedisRepository.save(redisRefreshToken);
    }


    // todo TEST용으로 추후 삭제
    public LoginResponse fakeLogin(String username) {
        Optional<Member> member = memberRepository.findByUsername(username);
        Member mem = member.get();
        TokenDto tokenDto = jwtTokenUtil.generateTokenDto(mem);
        createRedisRefreshTokenAndSave(mem.getUsername(), tokenDto.getRefreshToken());
        KakaoProfile kakaoProfile = KakaoProfile.builder()
                .id(1L)
                .properties(KakaoProfile.Properties.builder()
                        .nickname(mem.getNickname())
                        .profile_image(mem.getImageFileUrl())
                        .thumbnail_image(mem.getImageFileUrl())
                        .build())
                .build();
        return LoginResponse.of(mem.getId(), tokenDto, kakaoProfile);
    }


}

