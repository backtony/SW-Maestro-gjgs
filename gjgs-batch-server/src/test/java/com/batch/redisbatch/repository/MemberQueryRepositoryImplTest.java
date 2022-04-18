package com.batch.redisbatch.repository;

import com.batch.redisbatch.config.QuerydslConfig;
import com.batch.redisbatch.domain.Member;
import com.batch.redisbatch.domain.Zone;
import com.batch.redisbatch.dto.MemberFcmDto;
import com.batch.redisbatch.enums.Authority;
import com.batch.redisbatch.enums.Sex;
import com.batch.redisbatch.repository.impl.MemberQueryRepositoryImpl;
import com.batch.redisbatch.repository.interfaces.MemberRepository;
import com.batch.redisbatch.repository.interfaces.ZoneRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Import({MemberQueryRepositoryImpl.class, QuerydslConfig.class})
class MemberQueryRepositoryImplTest {

    @Autowired EntityManager em;
    @Autowired JPAQueryFactory query;
    @Autowired MemberRepository memberRepository;
    @Autowired MemberQueryRepositoryImpl memberQueryRepository;
    @Autowired ZoneRepository zoneRepository;


    @DisplayName("id로 멤버 찾아서 MemberFcmDto로 반환 ")
    @Test
    void findOnlyFcmById() throws Exception{
        //given
        Zone zone = zoneRepository.save(Zone.builder().build());

        Member member = Member.builder()
                .username("mem")
                .authority(Authority.ROLE_USER)
                .nickname("mem")
                .name("mem")
                .phone("01000000000")
                .imageFileUrl("mem")
                .profileText("mem")
                .directorText("mem")
                .age(25)
                .sex(Sex.M)
                .zone(zone)
                .fcmToken("fcmToken")
                .eventAlarm(true)
                .build();

        Member save = memberRepository.save(member);

        em.flush();
        em.clear();
        //when
        MemberFcmDto memberFcmDto = memberQueryRepository.findOnlyFcmById(save.getId()).get();

        //then
        assertAll(
                () -> assertEquals(save.getId(),memberFcmDto.getId()),
                () -> assertEquals(save.getFcmToken(),memberFcmDto.getFcmToken())
        );
    }

}

