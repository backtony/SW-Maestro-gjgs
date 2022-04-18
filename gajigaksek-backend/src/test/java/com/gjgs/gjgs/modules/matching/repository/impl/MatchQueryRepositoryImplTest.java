package com.gjgs.gjgs.modules.matching.repository.impl;

import com.gjgs.gjgs.config.repository.SetUpMemberRepository;
import com.gjgs.gjgs.modules.matching.dto.MatchingRequest;
import com.gjgs.gjgs.modules.matching.dto.MemberFcmIncludeNicknameDto;
import com.gjgs.gjgs.modules.matching.entity.Matching;
import com.gjgs.gjgs.modules.matching.repository.interfaces.MatchingQueryRepository;
import com.gjgs.gjgs.modules.matching.repository.interfaces.MatchingRepository;
import com.gjgs.gjgs.modules.member.entity.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MatchQueryRepositoryImplTest extends SetUpMemberRepository {

    @Autowired MatchingQueryRepository matchingQueryRepository;
    @Autowired MatchingRepository matchingRepository;

    MatchingRequest matchingRequest;
    Member member1;
    Member member2;
    Matching mc1;
    Matching mc2;

    @BeforeEach
    void setup(){
        matchingRequest = MatchingRequest.builder()
                .zoneId(zone.getId())
                .categoryId(category.getId())
                .dayType("MON|TUE")
                .timeType("AFTERNOON")
                .preferMemberCount(4)
                .build();
        member1 = anotherMembers.get(0);
        member2 = anotherMembers.get(1);
        mc1 = matchingRepository.save(Matching.of(matchingRequest,member1));
        mc2 = matchingRepository.save(Matching.of(matchingRequest,member2));
    }

    @AfterEach
    void teardown(){
        matchingRepository.deleteAll();
    }

    @DisplayName("username으로 매칭 중인지 확인")
    @Test
    void exists_by_username() throws Exception {
        // given
        flushAndClear();

        // when then
        assertTrue(matchingQueryRepository.existsByUsername(member1.getUsername()));
    }

    @DisplayName("매칭폼과 카테고리 기반으로 매칭되는 멤버들 찾기")
    @Test
    void find_by_match_form_and_category() throws Exception {
        // given
        flushAndClear();

        //when
        List<MemberFcmIncludeNicknameDto> dtoList = matchingQueryRepository.findMemberFcmDtoByMatchForm(matchingRequest);

        //then
        assertAll(
                () -> assertEquals(2, dtoList.size()),
                () -> assertEquals(member1.getId(), dtoList.get(0).getMemberId()),
                () -> assertEquals(member2.getId(), dtoList.get(1).getMemberId())
        );
    }


}
