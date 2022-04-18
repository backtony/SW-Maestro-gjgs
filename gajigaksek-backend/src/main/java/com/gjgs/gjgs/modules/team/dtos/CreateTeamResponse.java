package com.gjgs.gjgs.modules.team.dtos;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CreateTeamResponse {

    private String createdTeamId;

    public static CreateTeamResponse of(Long teamId) {
        return CreateTeamResponse.builder()
                .createdTeamId(String.valueOf(teamId))
                .build();
    }
}
