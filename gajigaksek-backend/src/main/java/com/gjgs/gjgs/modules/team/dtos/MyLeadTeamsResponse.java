package com.gjgs.gjgs.modules.team.dtos;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MyLeadTeamsResponse {

    @Builder.Default
    private List<MyLeadTeamsWithBulletin> myLeadTeams = new ArrayList<>();

    public static MyLeadTeamsResponse of(List<MyLeadTeamsWithBulletin> list) {
        return MyLeadTeamsResponse.builder()
                .myLeadTeams(list)
                .build();
    }

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class MyLeadTeamsWithBulletin {
        private Long teamId;
        private String teamName;
        private boolean hasBulletin;
        private boolean teamsRecruit;
        private BulletinData bulletinData;
        private LectureData lectureData;

        public void setBulletinLectureData(Long bulletinId, String bulletinTitle, String age, String time, String text, String day, Boolean teamsRecruit, Long lectureId, Integer priceOne, Integer priceTwo, Integer priceThree, Integer priceFour, String lectureTitle, String lectureImageUrl, Long zoneId, Long categoryId) {
            this.hasBulletin = true;
            this.teamsRecruit = teamsRecruit;
            this.bulletinData = BulletinData.builder()
                    .bulletinId(bulletinId)
                    .bulletinTitle(bulletinTitle)
                    .age(age)
                    .time(time)
                    .text(text)
                    .day(day)
                    .build();
            this.lectureData = LectureData.builder()
                    .lectureId(lectureId)
                    .lectureTitle(lectureTitle)
                    .lectureImageUrl(lectureImageUrl)
                    .zoneId(zoneId)
                    .categoryId(categoryId)
                    .priceOne(priceOne)
                    .priceTwo(priceTwo)
                    .priceThree(priceThree)
                    .priceFour(priceFour)
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class BulletinData {
        private Long bulletinId;
        private String bulletinTitle;
        private String age;
        private String time;
        private String text;
        private String day;
    }

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class LectureData {
        private Long lectureId;
        private String lectureTitle;
        private String lectureImageUrl;
        private Long zoneId;
        private Long categoryId;
        private int priceOne;
        private int priceTwo;
        private int priceThree;
        private int priceFour;
    }
}
