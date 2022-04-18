package com.gjgs.gjgs.modules.member.dto.login;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {

    private String grantType;

    private String accessToken;

    private String refreshToken;

    private Long accessTokenExpiresIn;

    private Long refreshTokenExpiresIn;

    public static TokenDto of(String grantType, String accessToken, String refreshToken, Long accessTokenExpiresIn, Long refreshTokenExpiresIn) {
        return TokenDto.builder()
                .grantType(grantType)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn)
                .refreshToken(refreshToken)
                .refreshTokenExpiresIn(refreshTokenExpiresIn)
                .build();
    }
}