package com.gjgs.gjgs.modules.lecture.repositories.participant;

import com.gjgs.gjgs.config.repository.SetUpLectureTeamBulletinRepository;
import com.gjgs.gjgs.modules.dummy.ScheduleDummy;
import com.gjgs.gjgs.modules.lecture.entity.Participant;
import com.gjgs.gjgs.modules.lecture.entity.Schedule;
import com.gjgs.gjgs.modules.lecture.repositories.schedule.ScheduleRepository;
import com.gjgs.gjgs.modules.member.entity.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.gjgs.gjgs.modules.lecture.enums.ScheduleStatus.RECRUIT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ParticipantQueryRepositoryImplTest extends SetUpLectureTeamBulletinRepository {

    @Autowired ParticipantQueryRepository participantQueryRepository;
    @Autowired ScheduleRepository scheduleRepository;
    @Autowired ParticipantRepository participantRepository;

    @Test
    @DisplayName("Participant 한번에 지우기")
    void delete_participant_test()throws Exception {

        // given
        Schedule schedule = scheduleRepository.save(ScheduleDummy.createSchedule(lecture));
        Participant participant1 = participantRepository.save(Participant.builder().member(leader).schedule(schedule).build());
        Participant participant2 = participantRepository.save(Participant.builder().member(anotherMembers.get(0)).schedule(schedule).build());
        flushAndClear();

        // when
        long count = participantQueryRepository.deleteParticipants(List.of(participant1.getId(), participant2.getId()));

        // then
        assertEquals(count, 2);
    }

    @Test
    @DisplayName("scheduleId와 memberIdList로 참가자 데이터 한번에 지우기")
    void delete_Participants_By_ScheduleId_MemberId()throws Exception {

        // given
        Schedule schedule = scheduleRepository.save(ScheduleDummy.createSchedule(lecture));
        participantRepository.save(Participant.builder().member(leader).schedule(schedule).build());
        Member member = anotherMembers.get(0);
        participantRepository.save(Participant.builder().member(member).schedule(schedule).build());
        flushAndClear();

        // when
        long count = participantQueryRepository.deleteParticipantsByScheduleIdMemberId(schedule.getId(), List.of(leader.getId(), member.getId()));

        // then
        assertEquals(count, 2);
    }

    @Test
    @DisplayName("해당 스케줄에 참가하는 인원인지 확인하기")
    void exist_Participant_By_ScheduleId_MemberId() throws Exception {

        // given
        Schedule schedule = scheduleRepository.save(createSchedule());
        Member member = anotherMembers.get(0);
        participantRepository.save(Participant.of(member, schedule));
        flushAndClear();

        // when, then
        Assertions.assertTrue(participantQueryRepository.existParticipantByScheduleIdMemberId(schedule.getId(), member.getId()));
    }

    private Schedule createSchedule() {
        return Schedule.builder()
                .lecture(lecture)
                .scheduleStatus(RECRUIT)
                .currentParticipants(0)
                .lectureDate(LocalDate.now())
                .startTime(LocalTime.now())
                .endTime(LocalTime.now().plusMinutes(30))
                .build();
    }
}
