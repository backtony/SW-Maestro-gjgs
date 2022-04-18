package com.gjgs.gjgs.modules.bulletin.dto;


import com.gjgs.gjgs.modules.bulletin.enums.Age;
import com.gjgs.gjgs.modules.member.dto.mypage.MyBulletinDto;
import com.gjgs.gjgs.modules.member.dto.mypage.MyBulletinResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MyBulletinResponseDtoTest {


    @DisplayName("MyBulletinDtoResponse 생성")
    @Test
    void my_bulletin_dto_response() throws Exception {
        //given
        MyBulletinDto myBulletinDto = MyBulletinDto.builder()
                .bulletinId(1L)
                .age(Age.THIRTY_TO_THIRTYFIVE)
                .currentPeople(4)
                .thumbnailImageFileUrl("testInmage")
                .timeType("AFTERNOON")
                .title("test")
                .zoneId(1L)
                .build();

        //when
        MyBulletinResponse myBulletinResponse = MyBulletinResponse.from(Arrays.asList(myBulletinDto));

        //then
        assertEquals(myBulletinDto, myBulletinResponse.getMyBulletinDtoList().get(0));
    }
}
