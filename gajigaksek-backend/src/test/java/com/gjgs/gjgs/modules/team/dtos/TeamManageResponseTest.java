package com.gjgs.gjgs.modules.team.dtos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeamManageResponseTest {

    @Test
    @DisplayName("DTO 리턴 테스트, accept")
    void return_team_manage_response_accept_test() throws Exception {
        // given, when
        TeamManageResponse res = TeamManageResponse
                .of(1L, 2L, true);

        // then
        assertAll(
                () -> assertEquals(res.getTeamId(), 1L),
                () -> assertEquals(res.getMemberId(), 2L),
                () -> assertTrue(res.isAccept()),
                () -> assertFalse(res.isReject())
        );
    }

    @Test
    @DisplayName("DTO 리턴 테스트, refuse")
    void return_team_manage_response_refuse_test() throws Exception {
        // given, when
        TeamManageResponse res = TeamManageResponse
                .of(1L, 2L, false);

        // then
        assertAll(
                () -> assertEquals(res.getTeamId(), 1L),
                () -> assertEquals(res.getMemberId(), 2L),
                () -> assertFalse(res.isAccept()),
                () -> assertTrue(res.isReject())
        );
    }
}