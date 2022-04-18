package com.gjgs.gjgs.modules.notification.dto;


import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
public class MemberFcmDto {

    private Long memberId;

    private String fcmToken;

    @QueryProjection
    public MemberFcmDto(Long memberId, String fcmToken) {
        this.memberId = memberId;
        this.fcmToken = fcmToken;
    }


}
