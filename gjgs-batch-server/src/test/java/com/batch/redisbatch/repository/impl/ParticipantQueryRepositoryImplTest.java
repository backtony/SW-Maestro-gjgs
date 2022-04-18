package com.batch.redisbatch.repository.impl;

import com.batch.redisbatch.config.QuerydslConfig;
import com.batch.redisbatch.domain.Member;
import com.batch.redisbatch.domain.Zone;
import com.batch.redisbatch.domain.lecture.Lecture;
import com.batch.redisbatch.domain.lecture.Participant;
import com.batch.redisbatch.domain.lecture.Schedule;
import com.batch.redisbatch.dummy.LectureDummy;
import com.batch.redisbatch.dummy.MemberDummy;
import com.batch.redisbatch.dummy.ScheduleDummy;
import com.batch.redisbatch.repository.interfaces.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Import({QuerydslConfig.class,ParticipantQueryRepositoryImpl.class})
class ParticipantQueryRepositoryImplTest {

    @Autowired EntityManager em;
    @Autowired ZoneRepository zoneRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired LectureRepository lectureRepository;
    @Autowired ScheduleRepository scheduleRepository;
    @Autowired ParticipantRepository participantRepository;
    @Autowired ParticipantQueryRepository participantQueryRepository;

    @AfterEach
    void teardown(){
        participantRepository.deleteAll();
        scheduleRepository.deleteAll();
        lectureRepository.deleteAll();
        memberRepository.deleteAll();
        zoneRepository.deleteAll();
    }


    @DisplayName("스케줄에 참가한 인원 삭제하기")
    @Test
    void delete_schedule_participants() throws Exception {

        // given
        Zone zone = zoneRepository.save(createZone());
        Member member = memberRepository.save(MemberDummy.createMember(zone, 1));
        Member member2 = memberRepository.save(MemberDummy.createMember(zone, 2));
        Lecture lecture = lectureRepository.save(LectureDummy.createLecture());
        Schedule schedule = scheduleRepository.save(ScheduleDummy.createSchedule(lecture));
        participantRepository.save(Participant.builder().schedule(schedule).member(member).build());
        participantRepository.save(Participant.builder().schedule(schedule).member(member2).build());
        flushAndClear();

        // when, then
        List<Long> memberIdList = List.of(member.getId(), member2.getId());
        assertEquals(2, participantQueryRepository.deleteParticipantsByScheduleIdMemberId(schedule.getId(), memberIdList));
    }

    private Zone createZone() {
        return Zone.builder().build();
    }

    private void flushAndClear() {
        em.flush();
        em.clear();
    }
}