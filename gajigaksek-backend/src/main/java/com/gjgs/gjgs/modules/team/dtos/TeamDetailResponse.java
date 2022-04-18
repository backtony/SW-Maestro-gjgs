package com.gjgs.gjgs.modules.team.dtos;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TeamDetailResponse extends AbstractSetLeaderFavoriteLecture {

    private String teamName;
    private String day;
    private String time;
    private int applyPeople;
    private int maxPeople;
    private Long zoneId;
    private boolean iAmLeader;
    private TeamMembers teamsLeader;

    @Builder.Default
    private Set<Long> categoryList = new HashSet<>();

    @Builder.Default
    private Set<TeamMembers> teamMemberList = new HashSet<>();

    @Builder.Default
    private Set<FavoriteLecture> favoriteLectureList = new HashSet<>();

    @QueryProjection
    public TeamDetailResponse(String teamName, String day, String time, int applyPeople, int maxPeople, Long zoneId, TeamMembers teamsLeader, Set<Long> categoryList, Set<TeamMembers> teamMemberList, Set<FavoriteLecture> favoriteLectureList) {
        this.teamName = teamName;
        this.day = day;
        this.time = time;
        this.applyPeople = applyPeople;
        this.maxPeople = maxPeople;
        this.zoneId = zoneId;
        this.teamsLeader = teamsLeader;
        this.categoryList = categoryList;
        this.teamMemberList = teamMemberList;
        this.favoriteLectureList = favoriteLectureList;
    }


    public void clearEmptySet() {
        clearCategoryListEmptySet();
        clearTeamMemberListEmptySet();
        clearFavoriteLectureListEmptySet();
    }

    private void clearCategoryListEmptySet() {
        categoryList.removeIf(categoryId -> categoryId == null);
    }

    private void clearTeamMemberListEmptySet() {
        teamMemberList.removeIf(teamMembers -> teamMembers.getMemberId() == null);
    }

    private void clearFavoriteLectureListEmptySet() {
        favoriteLectureList.removeIf(favoriteLecture -> favoriteLecture.getLectureId() == null);
    }

    @Override
    public void setLeaderMyFavoriteLecture(Long memberId, List<Long> favoriteLectureIdList) {
        if (teamsLeader.getMemberId().equals(memberId)) {
            iAmLeader = true;
        }

        favoriteLectureList.forEach(favoriteLecture -> {
            if (favoriteLectureIdList.contains(favoriteLecture.getLectureId())) {
                favoriteLecture.myFavoriteLecture = true;
            }});
    }

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder
    @EqualsAndHashCode(of = "memberId")
    public static class TeamMembers {

        private Long memberId;
        private String imageURL;
        private String nickname;
        private String sex;
        private int age;
        private String text;

        @QueryProjection
        public TeamMembers(Long memberId, String imageURL, String nickname, String sex, int age, String text) {
            this.memberId = memberId;
            this.imageURL = imageURL;
            this.nickname = nickname;
            this.sex = sex;
            this.age = age;
            this.text = text;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    @EqualsAndHashCode(of = "lectureId")
    public static class FavoriteLecture {
        private Long lectureId;
        private Long lecturesZoneId;
        private String lecturesTitle;
        private int lecturesPrice;
        private String lecturesImageURL;
        private boolean myFavoriteLecture;

        @QueryProjection
        public FavoriteLecture(Long lectureId, Long lecturesZoneId, String lecturesTitle, int lecturesPrice, String lecturesImageURL) {
            this.lectureId = lectureId;
            this.lecturesZoneId = lecturesZoneId;
            this.lecturesTitle = lecturesTitle;
            this.lecturesPrice = lecturesPrice;
            this.lecturesImageURL = lecturesImageURL;
        }
    }
}
