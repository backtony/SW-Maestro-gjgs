package com.gjgs.gjgs.modules.member.repository.interfaces;


import com.gjgs.gjgs.config.repository.CustomDataRedisTest;
import com.gjgs.gjgs.modules.member.redis.RefreshToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@CustomDataRedisTest
class RefreshTokenRedisRepositoryTest {

    @Autowired RefreshTokenRedisRepository refreshTokenRedisRepository;

    @BeforeEach
    void clear(){
        refreshTokenRedisRepository.deleteAll();
    }

    @DisplayName("save")
    @Test
    void save() throws Exception{
        //given
        RefreshToken refreshTokenRedis = createRefreshToken();

        //when
        refreshTokenRedisRepository.save(refreshTokenRedis);

        //then
        RefreshToken findRefreshToken = refreshTokenRedisRepository.findById(refreshTokenRedis.getId()).get();
        assertAll(
                () -> assertEquals(refreshTokenRedis.getId(),findRefreshToken.getId()),
                () -> assertEquals(refreshTokenRedis.getRefreshToken(),findRefreshToken.getRefreshToken()),
                () -> assertEquals(refreshTokenRedis.getExpiration(),findRefreshToken.getExpiration())
        );
    }

    private RefreshToken createRefreshToken() {
        String username = "username";
        String refreshToken = "refreshToken";
        Long expiration = 3000L;
        RefreshToken refreshTokenRedis = RefreshToken.of(username, refreshToken, expiration);
        return refreshTokenRedis;
    }


}
