package com.gjgs.gjgs.modules.favorite.dto;

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
public class FavoriteBulletinDto {

    private Long bulletinId;

    private Long bulletinMemberId;

    @Lob
    private String thumbnailImageFileUrl;

    private Long zoneId;

    private String title;

    private Age age;

    private String timeType;

    private int currentPeople;

    private int maxPeople;

    @QueryProjection
    public FavoriteBulletinDto(Long bulletinId, Long bulletinMemberId, String thumbnailImageFileUrl, Long zoneId, String title, Age age, String timeType, int currentPeople, int maxPeople) {
        this.bulletinId = bulletinId;
        this.bulletinMemberId = bulletinMemberId;
        this.thumbnailImageFileUrl = thumbnailImageFileUrl;
        this.zoneId = zoneId;
        this.title = title;
        this.age = age;
        this.timeType = timeType;
        this.currentPeople = currentPeople;
        this.maxPeople = maxPeople;
    }
}
