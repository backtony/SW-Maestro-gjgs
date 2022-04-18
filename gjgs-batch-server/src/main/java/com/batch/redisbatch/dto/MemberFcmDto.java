package com.batch.redisbatch.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class MemberFcmDto {

    private Long id;

    private String fcmToken;

    @QueryProjection
    public MemberFcmDto(Long id, String fcmToken) {
        this.id = id;
        this.fcmToken = fcmToken;
    }
}
