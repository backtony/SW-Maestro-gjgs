package com.gjgs.gjgs.modules.team.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class DelegateLeaderResponse {

    private Long teamId;
    private Long changedLeaderId;
    private Long toTeamMemberId;

    public static DelegateLeaderResponse of(Long teamId, Long changedLeaderId, Long toTeamMemberId) {
        return DelegateLeaderResponse.builder()
                .teamId(teamId)
                .changedLeaderId(changedLeaderId)
                .toTeamMemberId(toTeamMemberId)
                .build();
    }
}
