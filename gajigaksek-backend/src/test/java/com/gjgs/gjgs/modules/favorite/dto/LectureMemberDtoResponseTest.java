package com.gjgs.gjgs.modules.favorite.dto;

import com.gjgs.gjgs.modules.dummy.LectureDtoDummy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LectureMemberDtoResponseTest {

    @DisplayName("LectureMemberDtoResponse 생성")
    @Test
    void create_lectureMemberDtoResponse() throws Exception {
        //given
        LectureMemberDto lectureMemberDto = LectureDtoDummy.createLectureMemberDto();

        //when
        LectureMemberDtoResponse lectureMemberDtoResponse = LectureMemberDtoResponse.from(Arrays.asList(lectureMemberDto));

        //then
        assertEquals(lectureMemberDto, lectureMemberDtoResponse.getLectureMemberDtoList().get(0));
    }
}
