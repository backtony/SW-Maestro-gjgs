package com.gjgs.gjgs.modules.team.dtos;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MyTeamListResponse {

    @Builder.Default
    private Set<MyTeam> myTeamList = new HashSet<>();

    public static MyTeamListResponse of(Set<MyTeam> myTeam) {
        return MyTeamListResponse.builder()
                .myTeamList(myTeam)
                .build();
    }

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder
    @EqualsAndHashCode(of = "teamId")
    public static class MyTeam {

        private Long teamId;
        private String teamName;
        private int applyPeople;
        private int maxPeople;
        @Builder.Default
        private Set<Long> categoryList = new HashSet<>();
        private boolean iAmLeader;

        @QueryProjection
        public MyTeam(Long teamId, String teamName, int applyPeople, int maxPeople, Set<Long> categoryIdList, boolean isLeader) {
            this.teamId = teamId;
            this.teamName = teamName;
            this.applyPeople = applyPeople;
            this.maxPeople = maxPeople;
            this.categoryList = categoryIdList;
            this.iAmLeader = isLeader;
        }
    }
}
