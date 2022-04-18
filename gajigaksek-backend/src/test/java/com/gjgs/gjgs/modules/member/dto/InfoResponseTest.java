package com.gjgs.gjgs.modules.member.dto;

import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.member.dto.mypage.InfoResponse;
import com.gjgs.gjgs.modules.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InfoResponseTest {
    @DisplayName("InfoDto 생성")
    @Test
    void create_infoDto() throws Exception {
        //given
        Member member = MemberDummy.createTestMember();

        //when
        InfoResponse infoResponse = InfoResponse.from(member);

        //then
        assertAll(
                () -> assertEquals(member.getImageFileUrl(), infoResponse.getImageFileUrl()),
                () -> assertEquals(member.getSex(), infoResponse.getSex()),
                () -> assertEquals(member.getAge(), infoResponse.getAge()),
                () -> assertEquals(member.getZone().getId(), infoResponse.getZoneId()),
                () -> assertEquals(member.getMemberCategories().size(), infoResponse.getCategoryIdList().size()),
                () -> assertEquals(member.getNickname(), infoResponse.getNickname()),
                () -> assertEquals(member.getProfileText(), infoResponse.getProfileText())
        );
    }
}
