package com.gjgs.gjgs.modules.matching.repository.interfaces;

import com.gjgs.gjgs.config.repository.SetUpMemberRepository;
import com.gjgs.gjgs.modules.matching.dto.MatchingRequest;
import com.gjgs.gjgs.modules.matching.entity.Matching;
import com.gjgs.gjgs.modules.member.entity.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


class MatchingRepositoryTest extends SetUpMemberRepository {

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

    @DisplayName("Ids를 이용해 벌크 delete 쿼리")
    @Test
    void delete_all_with_bulk_by_id() throws Exception {
        // given
        flushAndClear();

        //when
        matchingRepository.deleteAllWithBulkById(Arrays.asList(mc1.getMember().getId(), mc2.getMember().getId()));

        Optional<Matching> getMc1 = matchingRepository.findById(mc1.getId());
        Optional<Matching> getMc2 = matchingRepository.findById(mc2.getId());

        //then
        assertAll(
                () -> assertEquals(Optional.empty(), getMc1),
                () -> assertEquals(Optional.empty(), getMc2)
        );
    }

    @DisplayName("매칭 취소")
    @Test
    void cancel_matching() throws Exception{

        //when
        long id = matchingRepository.deleteMatchingByMemberId(member1.getId());

        //then
        assertEquals(1,id);
    }
}
