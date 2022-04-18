package com.gjgs.gjgs.modules.team.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ModifyTeamResponse {

    private Long modifiedTeamId;

    public static ModifyTeamResponse of(Long teamId) {
        return ModifyTeamResponse.builder()
                .modifiedTeamId(teamId)
                .build();
    }
}
