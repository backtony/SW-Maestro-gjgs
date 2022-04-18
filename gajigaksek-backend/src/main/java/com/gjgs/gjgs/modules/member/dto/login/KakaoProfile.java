package com.gjgs.gjgs.modules.member.dto.login;


import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KakaoProfile {
    private Long id;
    private Properties properties;

    @Getter
    @Setter
    @ToString
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Properties {
        private String nickname;
        private String thumbnail_image;
        private String profile_image;

    }
}