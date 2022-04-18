package com.gjgs.gjgs.modules.matching.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
public class MemberFcmIncludeNicknameDto {

    private Long memberId;

    private String fcmToken;

    private String nickname;

    @QueryProjection
    public MemberFcmIncludeNicknameDto(Long memberId, String fcmToken, String nickname) {
        this.memberId = memberId;
        this.fcmToken = fcmToken;
        this.nickname = nickname;
    }

    public static MemberFcmIncludeNicknameDto of (Long memberId, String fcmToken, String nickname){
        return MemberFcmIncludeNicknameDto.builder()
                .memberId(memberId)
                .fcmToken(fcmToken)
                .nickname(nickname)
                .build();
    }

}
