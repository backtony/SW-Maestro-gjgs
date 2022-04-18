package com.gjgs.gjgs.modules.member.dto;

import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.member.dto.mypage.DirectorMyPageResponse;
import com.gjgs.gjgs.modules.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DirectorMyPageResponseTest {
    @DisplayName("DirectorMyPageDto 생성")
    @Test
    void create_directorMyPageDto() throws Exception {
        //given
        Member member = MemberDummy.createTestMember();

        //when
        DirectorMyPageResponse directorMyPageResponse = DirectorMyPageResponse.from(member);

        //then
        assertAll(
                () -> assertEquals(member.getImageFileUrl(), directorMyPageResponse.getImageFileUrl()),
                () -> assertEquals(member.getNickname(), directorMyPageResponse.getNickname()),
                () -> assertEquals(member.getName(), directorMyPageResponse.getName()),
                () -> assertEquals(member.getPhone(), directorMyPageResponse.getPhone()),
                () -> assertEquals(member.getDirectorText(), directorMyPageResponse.getDirectorText())
        );
    }
}
