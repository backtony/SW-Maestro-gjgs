package com.gjgs.gjgs.modules.member.repository.interfaces;

import com.gjgs.gjgs.config.repository.CustomDataRedisTest;
import com.gjgs.gjgs.modules.member.redis.LogoutAccessToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@CustomDataRedisTest
class LogoutAccessTokenRedisRepositoryTest {

    @Autowired
    LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;

    @BeforeEach
    void clear(){
        logoutAccessTokenRedisRepository.deleteAll();
    }

    @DisplayName("save")
    @Test
    void save() throws Exception{
        //given
        LogoutAccessToken logoutAccessToken = createLogoutAccessToken();

        //when
        logoutAccessTokenRedisRepository.save(logoutAccessToken);


        //then
        LogoutAccessToken find = logoutAccessTokenRedisRepository.findById(logoutAccessToken.getId()).get();

        assertAll(
                () -> assertEquals(logoutAccessToken.getId(),find.getId()),
                () -> assertEquals(logoutAccessToken.getUsername(),find.getUsername()),
                () -> assertEquals(logoutAccessToken.getExpiration(),find.getExpiration())
        );


    }

    private LogoutAccessToken createLogoutAccessToken() {
        String accessToken = "accessToken";
        String username = "username";
        Long expiration = 3000L;
        LogoutAccessToken logoutAccessToken = LogoutAccessToken.of(accessToken, username, expiration);
        return logoutAccessToken;
    }
}
