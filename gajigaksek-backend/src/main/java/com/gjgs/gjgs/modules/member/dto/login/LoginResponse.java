package com.gjgs.gjgs.modules.member.dto.login;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    private TokenDto tokenDto;

    private Long memberId;

    // 카카오 관련 필드
    private Long id;

    private String imageFileUrl;

    public static LoginResponse from(KakaoProfile kakaoProfile) {
        return LoginResponse.builder()
                .id(kakaoProfile.getId())
                .imageFileUrl(kakaoProfile.getProperties().getProfile_image())
                .build();
    }

    public static LoginResponse of(Long memberId, TokenDto tokenDto, KakaoProfile kakaoProfile) {
        return LoginResponse.builder()
                .id(kakaoProfile.getId())
                .imageFileUrl(kakaoProfile.getProperties().getProfile_image())
                .memberId(memberId)
                .tokenDto(tokenDto)
                .build();
    }

    public static LoginResponse of(Long memberId, TokenDto tokenDto, Long id, String url) {
        return LoginResponse.builder()
                .id(id)
                .imageFileUrl(url)
                .memberId(memberId)
                .tokenDto(tokenDto)
                .build();
    }


}
