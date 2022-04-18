package com.gjgs.gjgs.modules.member.dto;

import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.dummy.ZoneDummy;
import com.gjgs.gjgs.modules.member.dto.myinfo.MyPageEditResponse;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.zone.entity.Zone;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyPageEditResponseTest {


    @DisplayName("MyPageEditDto 생성")
    @Test
    void create_myPageEditDto() throws Exception {
        //given
        Member member = MemberDummy.createTestMember();
        Zone zone = ZoneDummy.createZone();
        member.changeZone(zone);

        //when
        MyPageEditResponse myPageEditResponse = MyPageEditResponse.from(member);

        //then
        assertAll(
                () -> assertEquals(member.getImageFileUrl(), myPageEditResponse.getImageFileUrl()),
                () -> assertEquals(member.getNickname(), myPageEditResponse.getNickname()),
                () -> assertEquals(member.getName(), myPageEditResponse.getName()),
                () -> assertEquals(member.getNickname(), myPageEditResponse.getNickname()),
                () -> assertEquals(member.getPhone(), myPageEditResponse.getPhone()),
                () -> assertEquals(member.getAuthority(), myPageEditResponse.getAuthority()),
                () -> assertEquals(member.getProfileText(), myPageEditResponse.getProfileText()),
                () -> assertEquals(member.getDirectorText(), myPageEditResponse.getDirectorText()),
                () -> assertEquals(member.getSex(), myPageEditResponse.getSex()),
                () -> assertEquals(member.getAge(), myPageEditResponse.getAge()),
                () -> assertEquals(member.getMemberCategories().size(), myPageEditResponse.getMemberCategoryId().size()),
                () -> assertEquals(member.getZone().getId(), myPageEditResponse.getZoneId()),
                () -> assertEquals(zone, member.getZone())

        );
    }
}
