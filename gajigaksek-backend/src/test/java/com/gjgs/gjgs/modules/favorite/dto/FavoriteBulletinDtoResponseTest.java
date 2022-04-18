package com.gjgs.gjgs.modules.favorite.dto;

import com.gjgs.gjgs.modules.dummy.BulletinDtoDummy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FavoriteBulletinDtoResponseTest {

    @DisplayName("BulletinMemberDtoResponse 생성")
    @Test
    void create_bulletinMemberDtoResponse() throws Exception {
        //given
        FavoriteBulletinDto favoriteBulletinDto = BulletinDtoDummy.createFavoriteBulletinDto();
        //when
        FavoriteBulletinDtoResponse favoriteBulletinDtoResponse = FavoriteBulletinDtoResponse.from(Arrays.asList(favoriteBulletinDto));

        //then
        assertEquals(favoriteBulletinDto, favoriteBulletinDtoResponse.getFavoriteBulletinDtoList().get(0));
    }
}
