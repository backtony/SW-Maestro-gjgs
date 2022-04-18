package com.gjgs.gjgs.modules.member.dto;


import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.member.dto.login.TokenDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TokenDtoTest {

    @DisplayName("tokenDto 생성")
    @Test
    void create_tokenDto() throws Exception {
        //given
        TokenDto testTokenDto = MemberDummy.generateToken();

        //when
        TokenDto tokenDto = createTokenDto(testTokenDto);

        //then
        assertAll(
                () -> assertEquals(testTokenDto.getGrantType(), tokenDto.getGrantType()),
                () -> assertEquals(testTokenDto.getAccessToken(), tokenDto.getAccessToken()),
                () -> assertEquals(testTokenDto.getRefreshToken(), tokenDto.getRefreshToken()),
                () -> assertEquals(testTokenDto.getRefreshTokenExpiresIn(), tokenDto.getRefreshTokenExpiresIn()),
                () -> assertEquals(testTokenDto.getAccessTokenExpiresIn(), tokenDto.getAccessTokenExpiresIn())
        );
    }

    private TokenDto createTokenDto(TokenDto testTokenDto) {
        return TokenDto.of(
                testTokenDto.getGrantType(),
                testTokenDto.getAccessToken(),
                testTokenDto.getRefreshToken(),
                testTokenDto.getAccessTokenExpiresIn(),
                testTokenDto.getRefreshTokenExpiresIn()
        );
    }

}
