package com.gjgs.gjgs.modules.bulletin.dto.search;

import com.gjgs.gjgs.modules.bulletin.enums.Age;
import com.gjgs.gjgs.modules.lecture.dtos.FavoriteResponse;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
public class BulletinSearchResponse extends FavoriteResponse {

    private Long bulletinId;
    private String lectureImageUrl;
    private Long zoneId;
    private Long categoryId;
    private String bulletinTitle;
    private String age;
    private String time;
    private int nowMembers;
    private int maxMembers;

    @QueryProjection
    public BulletinSearchResponse(Long bulletinId, String lectureImageUrl, Long zoneId, Long categoryId, String bulletinTitle, Age age, String time, int nowMembers, int maxMembers) {
        this.bulletinId = bulletinId;
        this.lectureImageUrl = lectureImageUrl;
        this.zoneId = zoneId;
        this.categoryId = categoryId;
        this.bulletinTitle = bulletinTitle;
        this.age = age.name();
        this.time = time;
        this.nowMembers = nowMembers;
        this.maxMembers = maxMembers;
        this.myFavorite = false;
    }

    public void changeMyFavorite(List<Long> myFavoriteBulletinIdList) {
        if (myFavoriteBulletinIdList.contains(bulletinId)) {
            this.myFavorite = true;
        }
    }

    @Override
    public void changeMyFavoriteContents(List<Long> favoriteContentsIdList) {
        if (favoriteContentsIdList.contains(bulletinId)) {
            myFavorite = true;
        }
    }
}
