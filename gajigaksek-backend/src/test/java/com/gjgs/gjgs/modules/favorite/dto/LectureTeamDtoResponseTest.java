package com.gjgs.gjgs.modules.favorite.dto;

import com.gjgs.gjgs.modules.dummy.LectureDtoDummy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LectureTeamDtoResponseTest {

    @DisplayName("LectureTeamDtoResponse 생성")
    @Test
    void create_lectureTeamDtoResponse() throws Exception {
        //given
        LectureTeamDto lectureTeamDto = LectureDtoDummy.createLectureTeamDto();

        //when
        LectureTeamDtoResponse lectureTeamDtoResponse = LectureTeamDtoResponse.from(Arrays.asList(lectureTeamDto));

        //then
        assertEquals(lectureTeamDto, lectureTeamDtoResponse.getLectureTeamDtoList().get(0));
    }
}
