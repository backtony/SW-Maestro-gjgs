package com.gjgs.gjgs.modules.bulletin.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BulletinIdResponse {

    private Long bulletinId;
    private Long teamId;
    private Long lectureId;

    public static BulletinIdResponse of(Long bulletinId) {
        return BulletinIdResponse.builder()
                .bulletinId(bulletinId)
                .build();
    }

    public static BulletinIdResponse of(Long bulletinId, Long lectureId) {
        return BulletinIdResponse.builder()
                .bulletinId(bulletinId)
                .lectureId(lectureId)
                .build();
    }

    public static BulletinIdResponse of(Long bulletinId, Long teamId, Long lectureId) {
        return BulletinIdResponse.builder()
                .bulletinId(bulletinId)
                .teamId(teamId)
                .lectureId(lectureId)
                .build();
    }
}
