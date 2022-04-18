package com.gjgs.gjgs.modules.utils;

import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.member.dto.login.TokenDto;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.enums.Authority;
import com.gjgs.gjgs.modules.utils.jwt.JwtTokenUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class JwtUtilTest {

    String sampleSecretKey =
            "dGpkcmxhcmxkaGtzcmhxamF0anJ0aHZteG1kbnBkamFrZHB0bXhtZmgxMnJsMThxanN4bGFkbWZocmt3bHJrcnRvcmRsZmtzbXN3bmNwZmh2bWZod3ByeG1mbWZ3bHNnb2Rna3JoZGxUdG1xc2xla3drZnFueGtyZW1mbHFzbGVrZ2hrZGx4bGQK";

    JwtTokenUtil jwtTokenUtil
            = new JwtTokenUtil(sampleSecretKey);


    @DisplayName("전체 토큰 생성 by Memeber")
    @Test
    void generate_tokenDto_by_member() throws Exception {
        //given
        Member member = MemberDummy.createMemberForTokenGenerate();
        //when
        TokenDto tokenDto = jwtTokenUtil.generateTokenDto(member);

        //then
        assertAll(
                () -> assertNotNull(tokenDto.getAccessToken()),
                () -> assertNotNull(tokenDto.getRefreshToken()),
                () -> assertNotNull(tokenDto.getAccessTokenExpiresIn()),
                () -> assertNotNull(tokenDto.getRefreshTokenExpiresIn()),
                () -> assertNotNull(tokenDto.getGrantType()),
                () -> assertEquals(jwtTokenUtil.getUsernameFromToken(tokenDto.getAccessToken()), member.getUsername()),
                () -> assertEquals(jwtTokenUtil.getUsernameFromToken(tokenDto.getRefreshToken()), member.getUsername())
        );
    }

    @DisplayName("전체 토큰 생성 by Authentication")
    @Test
    void generate_tokenDto_by_authentication() throws Exception {
        //given
        Authentication authentication = MemberDummy.createAuthentication();

        //when
        TokenDto tokenDto = jwtTokenUtil.generateTokenDto(authentication);

        //then
        assertAll(
                () -> assertNotNull(tokenDto.getAccessToken()),
                () -> assertNotNull(tokenDto.getRefreshToken()),
                () -> assertNotNull(tokenDto.getAccessTokenExpiresIn()),
                () -> assertNotNull(tokenDto.getRefreshTokenExpiresIn()),
                () -> assertNotNull(tokenDto.getGrantType()),
                () -> assertEquals(jwtTokenUtil.getUsernameFromToken(tokenDto.getAccessToken()), authentication.getName()),
                () -> assertEquals(jwtTokenUtil.getUsernameFromToken(tokenDto.getRefreshToken()), authentication.getName())
        );
    }

    @DisplayName("AccessToken 생성 by Authentication")
    @Test
    void generate_accessToken_by_authentication() throws Exception {
        //given
        Authentication authentication = MemberDummy.createAuthentication();

        //when
        String accessToken = jwtTokenUtil.generateAccessToken(authentication);

        //then
        assertEquals(jwtTokenUtil.getUsernameFromToken(accessToken), authentication.getName());

    }


    @DisplayName("토큰에서 Authentication 추출")
    @Test
    void get_authentication() throws Exception {
        //given
        Authentication authentication = MemberDummy.createAuthentication();

        //when
        TokenDto tokenDto = jwtTokenUtil.generateTokenDto(authentication);

        //then
        assertEquals(jwtTokenUtil.getAuthentication(tokenDto.getAccessToken(), Authority.ROLE_USER),
                authentication
        );
    }

    @DisplayName("생성된 토큰이 정상적인 시간으로 생성되었는지 확인")
    @Test
    void get_remainingSeconds() throws Exception {
        //given
        Member member = MemberDummy.createMemberForTokenGenerate();

        //when
        TokenDto tokenDto = jwtTokenUtil.generateTokenDto(member);

        //then
        assertThat(
                jwtTokenUtil.getRemainingMilliSeconds(tokenDto.getAccessToken()))
                .isLessThan(JwtTokenUtil.ACCESS_TOKEN_EXPIRE_TIME);
        assertThat(
                jwtTokenUtil.getRemainingMilliSeconds(tokenDto.getAccessToken()))
                .isGreaterThan(JwtTokenUtil.ACCESS_TOKEN_EXPIRE_TIME - 10000);
        assertThat(
                jwtTokenUtil.getRemainingMilliSeconds(tokenDto.getRefreshToken()))
                .isLessThan(JwtTokenUtil.REFRESH_TOKEN_EXPIRE_TIME);
        assertThat(
                jwtTokenUtil.getRemainingMilliSeconds(tokenDto.getRefreshToken()))
                .isGreaterThan(JwtTokenUtil.REFRESH_TOKEN_EXPIRE_TIME - 10000);
    }



    @DisplayName("유효한 토큰인지 확인")
    @Test
    void validate_token() throws Exception {
        //given
        Member member = MemberDummy.createMemberForTokenGenerate();
        //when
        TokenDto tokenDto = jwtTokenUtil.generateTokenDto(member);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();

        //then
        assertAll(
                () -> assertTrue(jwtTokenUtil.validateToken(accessToken)),
                () -> assertTrue(jwtTokenUtil.validateToken(refreshToken)),
                () -> assertFalse(jwtTokenUtil.validateToken(accessToken + "test")),
                () -> assertFalse(jwtTokenUtil.validateToken(refreshToken + "test"))
        );
    }

    @DisplayName("3분 남은 refresh 토큰 생성")
    @Test
    void generate_refreshToken_remain_3Day() throws Exception {
        //given
        Member member = MemberDummy.createMemberForTokenGenerate();

        //when
        String refreshToken = jwtTokenUtil.generateRefreshTokenRemain3Day(member);

        //then
        assertEquals(jwtTokenUtil.getUsernameFromToken(refreshToken), member.getUsername());
        assertThat(jwtTokenUtil.getRemainingMilliSeconds(refreshToken)).isLessThan(1000L * 60 * 60 * 24 * 3);
    }
}
