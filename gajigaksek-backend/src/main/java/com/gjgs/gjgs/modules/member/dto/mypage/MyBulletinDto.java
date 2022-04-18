package com.gjgs.gjgs.modules.member.dto.mypage;


import com.gjgs.gjgs.modules.bulletin.enums.Age;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Lob;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class MyBulletinDto {

    private Long bulletinId;

    @Lob
    private String thumbnailImageFileUrl;

    private Long zoneId;

    private String title;

    private Age age;

    private String timeType;

    private int currentPeople;

    private int maxPeople;

    private boolean status;

    @QueryProjection
    public MyBulletinDto(Long bulletinId, String thumbnailImageFileUrl, Long zoneId, String title, Age age, String timeType, int currentPeople, int maxPeople, boolean status) {
        this.bulletinId = bulletinId;
        this.thumbnailImageFileUrl = thumbnailImageFileUrl;
        this.zoneId = zoneId;
        this.title = title;
        this.age = age;
        this.timeType = timeType;
        this.currentPeople = currentPeople;
        this.maxPeople = maxPeople;
        this.status = status;
    }
}
