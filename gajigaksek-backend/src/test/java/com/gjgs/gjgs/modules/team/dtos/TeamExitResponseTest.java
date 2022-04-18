package com.gjgs.gjgs.modules.team.dtos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TeamExitResponseTest {

    @Test
    @DisplayName("리더가 팀을 나갈 경우")
    void exit_leader_test() throws Exception {

        // given
        Long exitTeamId = 1L;
        Long exitMemberId = 1L;

        // when
        TeamExitResponse res = TeamExitResponse.teamDelete(exitTeamId, exitMemberId);

        // then
        assertAll(
                () -> assertEquals(res.getTeamId(), exitTeamId),
                () -> assertEquals(res.getMemberId(), exitMemberId)
        );
    }

    @Test
    @DisplayName("팀원을 추방할 경우")
    void exit_team_member_test() throws Exception {

        // given
        Long exitTeamId = 1L;
        Long exitMemberId = 1L;

        // when
        TeamExitResponse res = TeamExitResponse.excludeMember(exitTeamId, exitMemberId);

        // then
        assertAll(
                () -> assertEquals(res.getTeamId(), exitTeamId),
                () -> assertEquals(res.getMemberId(), exitMemberId)
        );
    }
}