package com.gjgs.gjgs.modules.member.service;

import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.exception.member.InvalidAuthorityException;
import com.gjgs.gjgs.modules.exception.token.InvalidTokenException;
import com.gjgs.gjgs.modules.member.dto.login.KakaoProfile;
import com.gjgs.gjgs.modules.member.dto.login.LoginResponse;
import com.gjgs.gjgs.modules.member.dto.login.SignUpRequest;
import com.gjgs.gjgs.modules.member.dto.login.TokenDto;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.enums.Authority;
import com.gjgs.gjgs.modules.member.redis.RefreshToken;
import com.gjgs.gjgs.modules.member.repository.interfaces.LogoutAccessTokenRedisRepository;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberJdbcRepository;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberRepository;
import com.gjgs.gjgs.modules.member.repository.interfaces.RefreshTokenRedisRepository;
import com.gjgs.gjgs.modules.member.service.login.impl.KakaoService;
import com.gjgs.gjgs.modules.member.service.login.impl.LoginServiceImpl;
import com.gjgs.gjgs.modules.utils.jwt.JwtTokenUtil;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock KakaoService kakaoService;
    @Mock MemberRepository memberRepository;
    @Mock JwtTokenUtil jwtTokenUtil;
    @Mock RefreshTokenRedisRepository refreshTokenRedisRepository;
    @Mock LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;
    @Mock MemberJdbcRepository memberJdbcRepository;
    @Mock SecurityUtil securityUtil;

    @InjectMocks LoginServiceImpl loginService;

    @DisplayName("로그인 - 회원이 존재하지 않는 경우 kakao정보만 담아서 반환")
    @Test
    void login_fail_to_return_kakaoInfo() throws Exception {

        // given
        KakaoProfile kakaoProfile = createKakaoProfile();
        when(kakaoService.getKakaoProfile(any())).thenReturn(kakaoProfile);
        when(memberRepository.findByUsername(any())).thenReturn(Optional.empty());

        //when
        LoginResponse loginResponse = loginService.login("accessToken","fcmToken");

        //then
        assertAll(
                () -> assertNull(loginResponse.getTokenDto()),
                () -> assertNull(loginResponse.getMemberId()),
                () -> assertEquals(loginResponse.getId(), kakaoProfile.getId()),
                () -> assertEquals(loginResponse.getImageFileUrl(), kakaoProfile.getProperties().getProfile_image())
        );
    }

    @DisplayName("로그인 - 회원이 존재하는 경우")
    @Test
    void login_success_return_token() throws Exception {

        // given
        Member member = MemberDummy.createMemberForTokenGenerate();
        KakaoProfile kakaoProfile = createKakaoProfile();
        when(kakaoService.getKakaoProfile(any())).thenReturn(kakaoProfile);
        when(memberRepository.findByUsername(any())).thenReturn(Optional.of(member));
        when(jwtTokenUtil.generateTokenDto(member)).thenReturn(MemberDummy.generateToken());

        //when
        LoginResponse loginResponse = loginService.login("accessToken","fcmToken");

        //then
        assertAll(
                () -> assertEquals(kakaoProfile.getId(), loginResponse.getId()),
                () -> assertEquals(kakaoProfile.getProperties().getProfile_image(), loginResponse.getImageFileUrl()),
                () -> assertNotNull(loginResponse.getTokenDto()),
                () -> assertEquals("Bearer", loginResponse.getTokenDto().getGrantType()),
                () -> assertEquals("TestAccessToken", loginResponse.getTokenDto().getAccessToken()),
                () -> assertEquals(1000L * 60 * 360, loginResponse.getTokenDto().getAccessTokenExpiresIn()),
                () -> assertEquals("TestRefreshToken", loginResponse.getTokenDto().getRefreshToken()),
                () -> assertEquals(1000L * 60 * 60 * 24 * 14, loginResponse.getTokenDto().getRefreshTokenExpiresIn())


        );
    }

    @DisplayName("회원가입 + 로그인")
    @Test
    void save_and_login() throws Exception {
        //given
        SignUpRequest signUpRequest = MemberDummy.createSignupForm();
        Member member = MemberDummy.createTestMember();
        TokenDto tokenDto = createTokenDto("AccessToken", "RefreshToken");

        when(memberRepository.save(any())).thenReturn(member);
        when(jwtTokenUtil.generateTokenDto(ArgumentMatchers.any(Member.class))).thenReturn(tokenDto);

        //when
        LoginResponse loginResponse = loginService.saveAndLogin(signUpRequest);

        //then
        assertAll(
                () -> assertEquals(tokenDto, loginResponse.getTokenDto()),
                () -> assertEquals(signUpRequest.getImageFileUrl(), loginResponse.getImageFileUrl()),
                () -> assertEquals(null, loginResponse.getMemberId()),
                () -> assertEquals(signUpRequest.getId(), loginResponse.getId()),
                () -> verify(memberRepository,times(1)).save(any()),
                () -> verify(memberJdbcRepository,times(1)).insertMemberCategoryList(any(),any()),
                () -> verify(jwtTokenUtil,times(1)).generateTokenDto(ArgumentMatchers.any(Member.class)),
                () -> verify(jwtTokenUtil,times(1)).getRemainingMilliSeconds(any()),
                () -> verify(refreshTokenRedisRepository,times(1)).save(any())
        );
    }

    @DisplayName("토큰 재발급 - refresh 7일 미만 refresh, access 모두 재발급 ")
    @Test
    void reissue() throws Exception {
        //given
        String type = "Bearer";
        String refreshToken = "RefreshToken";
        String newAccessToken = "AccessToken";
        Long remainTime = 1L;
        TokenDto tokenDto = createTokenDto("newAccessToken", "newRefreshToken");
        RefreshToken refreshTokenRedis = RefreshToken.of("username", refreshToken, 16L);
        Authentication authentication = MemberDummy.createAuthentication();

        when(securityUtil.getCurrentUsername()).thenReturn(Optional.of("test"));
        when(securityUtil.getAuthority()).thenReturn(Optional.of(Authority.ROLE_USER));
        when(jwtTokenUtil.getAuthentication(any(),any())).thenReturn(authentication);
        when(refreshTokenRedisRepository.findById(any())).thenReturn(Optional.of(refreshTokenRedis));
        when(jwtTokenUtil.getRemainingMilliSeconds(any())).thenReturn(remainTime);
        when(jwtTokenUtil.generateTokenDto(ArgumentMatchers.any(Authentication.class))).thenReturn(tokenDto);

        //when
        TokenDto response = loginService.reissue(type + " " + refreshToken);


        //then
        assertAll(
                () -> assertEquals(type, response.getGrantType()),
                () -> assertNotEquals(newAccessToken, response.getAccessToken()),
                () -> assertNotEquals(refreshToken, response.getRefreshToken()),
                () -> assertEquals(remainTime, response.getAccessTokenExpiresIn()),
                () -> assertEquals(remainTime, response.getRefreshTokenExpiresIn())
        );
    }

    private TokenDto createTokenDto(String newAccessToken, String newRefreshToken) {
        return TokenDto.of("Bearer", newAccessToken,
                newRefreshToken, 1L, 1L);
    }

    @DisplayName("토큰 재발급 - refresh 7일 초과 accessToken만 새것으로 발급")
    @Test
    void reissue_only_access_token() throws Exception {
        //given
        String type = "Bearer";
        String refreshToken = "RefreshToken";
        String newAccessToken = "newAccessToken";
        Long remainTime = JwtTokenUtil.REFRESH_TOKEN_REISSUE_TIME + 1;
        RefreshToken refreshTokenRedis = RefreshToken.of("username", refreshToken, 16L);
        Authentication authentication = MemberDummy.createAuthentication();

        when(securityUtil.getCurrentUsername()).thenReturn(Optional.of("test"));
        when(securityUtil.getAuthority()).thenReturn(Optional.of(Authority.ROLE_USER));
        when(jwtTokenUtil.getAuthentication(any(),any())).thenReturn(authentication);
        when(refreshTokenRedisRepository.findById(any())).thenReturn(Optional.of(refreshTokenRedis));
        when(jwtTokenUtil.getRemainingMilliSeconds(any())).thenReturn(remainTime);
        when(jwtTokenUtil.generateAccessToken(ArgumentMatchers.any(Authentication.class))).thenReturn(newAccessToken);

        //when
        TokenDto response = loginService.reissue(type + " " + refreshToken);


        //then
        assertAll(
                () -> assertEquals(type, response.getGrantType()),
                () -> assertEquals(newAccessToken, response.getAccessToken()),
                () -> assertEquals(refreshToken, response.getRefreshToken()),
                () -> assertEquals(remainTime, response.getAccessTokenExpiresIn()),
                () -> assertEquals(remainTime, response.getRefreshTokenExpiresIn())
        );
    }

    @DisplayName("redis에서 가져온 refresh와 받은 refresh가 다른 경우")
    @Test
    void invalid_refresh_token() throws Exception {
        //given
        String refreshToken = "Bearer testRefreshToken";
        RefreshToken refreshTokenRedis = RefreshToken.of("username", "fakeRefreshToken", 16L);

        when(jwtTokenUtil.getAuthentication(any(),any())).thenReturn(MemberDummy.createAuthentication());
        when(refreshTokenRedisRepository.findById(any())).thenReturn(Optional.of(refreshTokenRedis));
        when(securityUtil.getCurrentUsername()).thenReturn(Optional.of("test"));
        when(securityUtil.getAuthority()).thenReturn(Optional.of(Authority.ROLE_USER));

        //when then
        assertThrows(InvalidTokenException.class, () -> loginService.reissue(refreshToken));

    }

    @DisplayName("웹 로그인")
    @Test
    void web_login() throws Exception{
        // given
        Member member = MemberDummy.createDirectorMemberForTokenGenerate();
        KakaoProfile kakaoProfile = createKakaoProfile();

        when(kakaoService.getKakaoProfile(any())).thenReturn(kakaoProfile);
        when(memberRepository.findByUsername(any())).thenReturn(Optional.of(member));
        when(jwtTokenUtil.generateTokenDto(member)).thenReturn(MemberDummy.generateToken());

        //when
        LoginResponse loginResponse = loginService.webLogin("accessToken","director");

        //then
        assertAll(
                () -> assertEquals(kakaoProfile.getId(), loginResponse.getId()),
                () -> assertEquals(kakaoProfile.getProperties().getProfile_image(), loginResponse.getImageFileUrl()),
                () -> assertNotNull(loginResponse.getTokenDto()),
                () -> assertEquals("Bearer", loginResponse.getTokenDto().getGrantType()),
                () -> assertEquals("TestAccessToken", loginResponse.getTokenDto().getAccessToken()),
                () -> assertEquals(1000L * 60 * 360, loginResponse.getTokenDto().getAccessTokenExpiresIn()),
                () -> assertEquals("TestRefreshToken", loginResponse.getTokenDto().getRefreshToken()),
                () -> assertEquals(1000L * 60 * 60 * 24 * 14, loginResponse.getTokenDto().getRefreshTokenExpiresIn())
        );
    }

    @DisplayName("일반 유저가 웹으로 로그인 한 경우 에러 처리")
    @Test
    void web_login_by_not_director() throws Exception{
        // given
        Member member = MemberDummy.createMemberForTokenGenerate();
        KakaoProfile kakaoProfile = createKakaoProfile();

        when(kakaoService.getKakaoProfile(any())).thenReturn(kakaoProfile);
        when(memberRepository.findByUsername(any())).thenReturn(Optional.of(member));

        // when then
        assertThrows(InvalidAuthorityException.class,
                () -> loginService.webLogin("accessToken","director"));
    }


    @DisplayName("로그아웃")
    @Test
    void logout() throws Exception{
        //given
        String token ="Bearer testToken";
        Member member = MemberDummy.createTestMember();

        when(securityUtil.getCurrentUsername()).thenReturn(Optional.of(member.getUsername()));
        when(memberRepository.findByUsername(any())).thenReturn(Optional.of(member));
        when(jwtTokenUtil.getRemainingMilliSeconds(any())).thenReturn(10L);

        //when
        loginService.logout(token,token);

        //then
        assertAll(
                () -> verify(refreshTokenRedisRepository).deleteById(member.getUsername()),
                () -> verify(logoutAccessTokenRedisRepository).save(any())
        );

    }

    private KakaoProfile createKakaoProfile() {
        KakaoProfile.Properties properties = KakaoProfile.Properties.builder()
                .nickname("test")
                .profile_image("test_profile_image")
                .thumbnail_image("test_thumbnail_image")
                .build();
        return KakaoProfile.builder()
                .id(1L)
                .properties(properties)
                .build();
    }

}

