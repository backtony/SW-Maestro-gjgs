package com.gjgs.gjgs.modules.favorite.dto;

import com.gjgs.gjgs.modules.dummy.TeamDummy;
import com.gjgs.gjgs.modules.team.entity.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyTeamAndIsIncludeFavoriteLectureDtoTest {

    @DisplayName("createTrueMyTeamAndIsIncludeFavoriteLectureDto 생성")
    @Test
    void create_MyTeamAndIsIncludeFavoriteLectureDto_include_true() throws Exception {
        //given
        Team team = TeamDummy.createTeamByIdAndTeamName(1L,"test");

        //when
        MyTeamAndIsIncludeFavoriteLectureDto dto =
                MyTeamAndIsIncludeFavoriteLectureDto.of(team,true);

        //then
        assertAll(
                () -> assertEquals(team.getId(), dto.getTeamId()),
                () -> assertEquals(team.getTeamName(), dto.getTeamName()),
                () -> assertTrue(dto.isInclude())
        );

    }

    @DisplayName("createFalseMyTeamAndIsIncludeFavoriteLectureDto 생성")
    @Test
    void createFalseMyTeamAndIsIncludeFavoriteLectureDto() throws Exception {
        //given
        Team team = TeamDummy.createTeamByIdAndTeamName(1L,"test");

        //when
        MyTeamAndIsIncludeFavoriteLectureDto dto =
                MyTeamAndIsIncludeFavoriteLectureDto.of(team,true);

        //then
        assertAll(
                () -> assertEquals(team.getId(), dto.getTeamId()),
                () -> assertEquals(team.getTeamName(), dto.getTeamName()),
                () -> assertTrue(dto.isInclude())
        );

    }
}
