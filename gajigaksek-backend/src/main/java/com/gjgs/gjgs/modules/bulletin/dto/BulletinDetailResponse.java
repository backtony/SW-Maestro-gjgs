package com.gjgs.gjgs.modules.bulletin.dto;

import com.gjgs.gjgs.modules.team.dtos.AbstractSetLeaderFavoriteLecture;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @NoArgsConstructor(access = AccessLevel.PROTECTED) @Builder
public class BulletinDetailResponse extends AbstractSetLeaderFavoriteLecture {

    private Long bulletinId;
    private String bulletinTitle;
    private String day;
    private String time;
    private String bulletinText;
    private String age;
    private BulletinsLecture bulletinsLecture;
    private BulletinsTeam bulletinsTeam;

    @QueryProjection
    public BulletinDetailResponse(Long bulletinId, String bulletinTitle, String day, String time, String bulletinText, String age, BulletinsLecture bulletinsLecture, BulletinsTeam bulletinsTeam) {
        this.bulletinId = bulletinId;
        this.bulletinTitle = bulletinTitle;
        this.day = day;
        this.time = time;
        this.bulletinText = bulletinText;
        this.age = age;
        this.bulletinsLecture = bulletinsLecture;
        this.bulletinsTeam = bulletinsTeam;
    }

    @Getter @Setter @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor @Builder
    public static class BulletinsLecture {

        private Long lectureId;
        private boolean myFavoriteLecture;
        private Long lecturesZoneId;
        private Long lecturesCategoryId;
        private String lecturesThumbnailUrl;
        private String lectureName;
        private int priceOne;
        private int priceTwo;
        private int priceThree;
        private int priceFour;

        @QueryProjection
        public BulletinsLecture(Long lectureId, Long lecturesZoneId, Long lecturesCategoryId, String lecturesThumbnailUrl, String lectureName, int priceOne, int priceTwo, int priceThree, int priceFour) {
            this.lectureId = lectureId;
            this.lecturesZoneId = lecturesZoneId;
            this.lecturesCategoryId = lecturesCategoryId;
            this.lecturesThumbnailUrl = lecturesThumbnailUrl;
            this.lectureName = lectureName;
            this.priceOne = priceOne;
            this.priceTwo = priceTwo;
            this.priceThree = priceThree;
            this.priceFour = priceFour;
        }
    }

    @Getter @Setter @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor @Builder
    public static class BulletinsTeam {
        private Long teamId;
        private boolean iAmLeader;
        private int currentPeople;
        private int maxPeople;
        private TeamMemberResponse leader;
        @Builder.Default
        private List<TeamMemberResponse> members = new ArrayList<>();

        @QueryProjection
        public BulletinsTeam(Long teamId, int currentPeople, int maxPeople, TeamMemberResponse leader, List<TeamMemberResponse> members) {
            this.teamId = teamId;
            this.currentPeople = currentPeople;
            this.maxPeople = maxPeople;
            this.leader = leader;
            this.members = members;
        }
    }

    @Getter @Setter @NoArgsConstructor(access = AccessLevel.PROTECTED) @Builder
    public static class TeamMemberResponse {

        private Long memberId;
        private String imageUrl;
        private String nickname;
        private String sex;
        private int age;
        private String text;

        @QueryProjection
        public TeamMemberResponse(Long memberId, String imageUrl, String nickname, String sex, int age, String text) {
            this.memberId = memberId;
            this.imageUrl = imageUrl;
            this.nickname = nickname;
            this.sex = sex;
            this.age = age;
            this.text = text;
        }
    }

    @Override
    public void setLeaderMyFavoriteLecture(Long memberId, List<Long> favoriteLectureIdList) {
        if (memberId.equals(this.getBulletinsTeam().getLeader().getMemberId())) {
            this.getBulletinsTeam().setIAmLeader(true);
        }

        if (favoriteLectureIdList.contains(this.getBulletinsLecture().getLectureId())) {
            this.getBulletinsLecture().setMyFavoriteLecture(true);
        }
    }
}
