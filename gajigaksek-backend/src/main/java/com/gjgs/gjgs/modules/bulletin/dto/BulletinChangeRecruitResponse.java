package com.gjgs.gjgs.modules.bulletin.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BulletinChangeRecruitResponse {

    private Long bulletinId;
    private String recruitStatus;

    public static BulletinChangeRecruitResponse of(Long bulletinId, boolean bulletinStatus) {
        return BulletinChangeRecruitResponse.builder()
                .bulletinId(bulletinId)
                .recruitStatus(checkRecruitStatus(bulletinStatus))
                .build();
    }

    private static String checkRecruitStatus(boolean bulletinStatus) {
        return bulletinStatus
                ? RecruitStatus.RECRUIT.name()
                : RecruitStatus.CLOSE.name();
    }
}
