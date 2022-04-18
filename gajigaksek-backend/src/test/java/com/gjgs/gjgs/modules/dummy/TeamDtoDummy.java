package com.gjgs.gjgs.modules.dummy;

import com.gjgs.gjgs.modules.team.dtos.CreateTeamRequest;

import java.util.List;

public class TeamDtoDummy {

    public static CreateTeamRequest buildCreateTeamRequest() {
        return createCreateTeamRequest();
    }

    private static CreateTeamRequest createCreateTeamRequest() {
        return CreateTeamRequest.builder()
                .categoryList(List.of(1L, 2L, 3L))
                .teamName("테스트")
                .dayType("테스트")
                .timeType("테스트")
                .maxPeople(4)
                .zoneId(1L)
                .build();
    }

    public static CreateTeamRequest createModifyTeamRequestLowerthanCurrentPeople() {
        return CreateTeamRequest.builder()
                .maxPeople(1)
                .build();
    }
}
