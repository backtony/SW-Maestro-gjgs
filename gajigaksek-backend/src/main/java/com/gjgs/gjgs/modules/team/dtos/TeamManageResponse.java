package com.gjgs.gjgs.modules.team.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TeamManageResponse {

    private Long teamId;
    private Long memberId;
    private boolean accept;
    private boolean reject;

    public static TeamManageResponse of(Long teamId, Long memberId, boolean acceptResult) {
        return getTeamManageResponse(teamId, memberId, acceptResult);
    }

    private static TeamManageResponse getTeamManageResponse(Long teamId, Long memberId, boolean acceptResult) {
        return TeamManageResponse.builder()
                .teamId(teamId)
                .memberId(memberId)
                .accept(acceptResult)
                .reject(!acceptResult)
                .build();
    }
}
