package com.gjgs.gjgs.modules.favorite.dto;


import com.gjgs.gjgs.modules.team.entity.Team;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyTeamAndIsIncludeFavoriteLectureDto {

    private Long teamId;

    private String teamName;

    private boolean isInclude;

    @QueryProjection
    public MyTeamAndIsIncludeFavoriteLectureDto(Long teamId, String teamName) {
        this.teamId = teamId;
        this.teamName = teamName;
    }

    public static MyTeamAndIsIncludeFavoriteLectureDto of(Team team, boolean bool){
        return MyTeamAndIsIncludeFavoriteLectureDto.builder()
                .teamId(team.getId())
                .teamName(team.getTeamName())
                .isInclude(bool)
                .build();
    }
}
