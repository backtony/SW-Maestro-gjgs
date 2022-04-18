package com.gjgs.gjgs.modules.member.dto;

import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.member.dto.mypage.MyPageResponse;
import com.gjgs.gjgs.modules.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MyPageResponseTest {
    @DisplayName("MyPageDto 생성")
    @Test
    void create_myPageDto() throws Exception {
        //given
        Member member = MemberDummy.createTestMember();

        //when
        MyPageResponse myPageResponse = MyPageResponse.from(member);

        //then
        assertAll(
                () -> assertEquals(member.getNickname(), myPageResponse.getNickname()),
                () -> assertEquals(member.getImageFileUrl(), myPageResponse.getImageFileUrl()),
                () -> assertEquals(member.getAuthority(), myPageResponse.getAuthority()),
                () -> assertEquals(member.getMemberCategories().size(), myPageResponse.getMemberCategoryIdList().size()),
                () -> assertEquals(member.getTotalReward(), myPageResponse.getTotalReward())

        );
    }
}
