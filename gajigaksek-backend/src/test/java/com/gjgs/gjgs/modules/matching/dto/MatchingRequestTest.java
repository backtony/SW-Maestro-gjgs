package com.gjgs.gjgs.modules.matching.dto;


import com.gjgs.gjgs.modules.dummy.MatchingDummy;
import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.matching.entity.Matching;
import com.gjgs.gjgs.modules.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MatchingRequestTest {

    @DisplayName("엔티티로 전환")
    @Test
    void to_entity() throws Exception {
        //given
        MatchingRequest matchingRequest = MatchingDummy.createMatchingRequest();
        Member member = MemberDummy.createTestMember();

        //when
        Matching matching =Matching.of(matchingRequest,member);

        //then
        assertAll(
                () -> assertEquals(1L, matching.getZone().getId()),
                () -> assertEquals(member, matching.getMember()),
                () -> assertEquals(1L, matching.getCategory().getId()),
                () -> assertEquals(matchingRequest.getDayType(), matching.getDayType()),
                () -> assertEquals(matchingRequest.getTimeType(), matching.getTimeType()),
                () -> assertEquals(matchingRequest.getPreferMemberCount(), matching.getPreferMemberCount())
        );
    }
}
