package com.gjgs.gjgs.modules.member.redis;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RefreshTokenTest {


    @DisplayName("RefreshToken 생성")
    @Test
    void create_refreshToken() throws Exception{
        //given
        String username = "username";
        String refreshToken = "refreshToken";
        Long expiration = 1000L;

        //when
        RefreshToken refreshTokenRedis = RefreshToken.of(username, refreshToken, expiration);

        //then
        assertAll(
                () -> assertEquals(username,refreshTokenRedis.getId()),
                () -> assertEquals(refreshToken,refreshTokenRedis.getRefreshToken()),
                () -> assertEquals(expiration/1000,refreshTokenRedis.getExpiration())
        );
    }
}
