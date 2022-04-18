package com.gjgs.gjgs.modules.member.dto;


import com.gjgs.gjgs.modules.matching.dto.MemberFcmIncludeNicknameDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MemberFcmIncludeNicknameDtoTest {

    @DisplayName("생성")
    @Test
    void create_MemberFcmIncludeNicknameDto() throws Exception{
        //given
        MemberFcmIncludeNicknameDto dto
                = MemberFcmIncludeNicknameDto.of(1L, "fcm", "nick");

        //when then
        assertAll(
                () -> assertEquals(1L,dto.getMemberId()),
                () -> assertEquals("fcm",dto.getFcmToken()),
                () -> assertEquals("nick",dto.getNickname())
        );
    }

}
