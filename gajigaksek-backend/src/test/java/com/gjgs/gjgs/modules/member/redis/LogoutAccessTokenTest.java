package com.gjgs.gjgs.modules.member.redis;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LogoutAccessTokenTest {

    @DisplayName("LogoutAccessToken 생성")
    @Test
    void create_logoutAccessToken() throws Exception{
        //given
        String accessToken = "accessToken";
        String username = "username";
        Long expiration = 1000L;

        //when
        LogoutAccessToken logoutAccessToken = LogoutAccessToken.of(accessToken, username, expiration);

        //then
        assertAll(
                () -> assertEquals(accessToken,logoutAccessToken.getId()),
                () -> assertEquals(username,logoutAccessToken.getUsername()),
                () -> assertEquals(expiration/1000,logoutAccessToken.getExpiration())
        );
    }
}
