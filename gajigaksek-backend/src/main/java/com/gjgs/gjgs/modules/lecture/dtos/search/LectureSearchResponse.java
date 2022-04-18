package com.gjgs.gjgs.modules.lecture.dtos.search;

import com.gjgs.gjgs.modules.lecture.dtos.FavoriteResponse;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class LectureSearchResponse extends FavoriteResponse {

    private Long lectureId;
    private boolean isOnlyGjgs;
    private String imageUrl;
    private String title;
    private Long zoneId;
    private int priceOne;
    private int priceTwo;
    private int priceThree;
    private int priceFour;

    @QueryProjection
    public LectureSearchResponse(Long lectureId, boolean isOnlyGjgs, String imageUrl, String title, Long zoneId, int priceOne, int priceTwo, int priceThree, int priceFour) {
        this.lectureId = lectureId;
        this.isOnlyGjgs = isOnlyGjgs;
        this.imageUrl = imageUrl;
        this.title = title;
        this.zoneId = zoneId;
        this.priceOne = priceOne;
        this.priceTwo = priceTwo;
        this.priceThree = priceThree;
        this.priceFour = priceFour;
        this.myFavorite = false;
    }

    @Override
    public void changeMyFavoriteContents(List<Long> favoriteContentsIdList) {
        if (favoriteContentsIdList.contains(lectureId)) {
            myFavorite = true;
        }
    }
}
