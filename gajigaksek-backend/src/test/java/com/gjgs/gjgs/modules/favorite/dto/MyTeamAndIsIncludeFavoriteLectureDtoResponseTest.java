package com.gjgs.gjgs.modules.favorite.dto;

import com.gjgs.gjgs.modules.dummy.TeamDummy;
import com.gjgs.gjgs.modules.team.entity.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MyTeamAndIsIncludeFavoriteLectureDtoResponseTest {

    @DisplayName("MyTeamAndIsIncludeFavoriteLectureDtoResponse 생성")
    @Test
    void create_myTeamAndIsIncludeFavoriteLectureDtoResponse() throws Exception {
        //given
        Team team = TeamDummy.createTeamByIdAndTeamName(1L,"test");
        MyTeamAndIsIncludeFavoriteLectureDto dto =
                MyTeamAndIsIncludeFavoriteLectureDto.of(team,true);

        //when
        MyTeamAndIsIncludeFavoriteLectureDtoResponse of
                = MyTeamAndIsIncludeFavoriteLectureDtoResponse.from(Arrays.asList(dto));

        //then
        assertEquals(dto, of.getMyTeamAndIsIncludeFavoriteLectureDtoList().get(0));
    }
}
