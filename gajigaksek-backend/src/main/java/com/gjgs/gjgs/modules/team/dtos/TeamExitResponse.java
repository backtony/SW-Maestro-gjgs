package com.gjgs.gjgs.modules.team.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TeamExitResponse {

    private Long teamId;
    private Long memberId;
    private String result;

    public static TeamExitResponse teamDelete(Long teamId, Long memberId) {
        return TeamExitResponse.builder()
                .teamId(teamId).memberId(memberId).result(TeamExitResult.DELETE.getDescription())
                .build();
    }

    public static TeamExitResponse excludeMember(Long teamId, Long memberId) {
        return TeamExitResponse.builder()
                .teamId(teamId).memberId(memberId).result(TeamExitResult.EXCLUDE.getDescription())
                .build();
    }

    public static TeamExitResponse exitMember(Long teamId, Long memberId) {
        return TeamExitResponse.builder()
                .teamId(teamId).memberId(memberId).result(TeamExitResult.EXIT.getDescription())
                .build();
    }

}
