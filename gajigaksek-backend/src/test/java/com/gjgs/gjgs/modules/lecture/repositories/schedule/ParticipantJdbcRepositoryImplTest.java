package com.gjgs.gjgs.modules.lecture.repositories.schedule;

import com.gjgs.gjgs.config.repository.SetUpLectureTeamBulletinRepository;
import com.gjgs.gjgs.modules.dummy.ScheduleDummy;
import com.gjgs.gjgs.modules.lecture.entity.Participant;
import com.gjgs.gjgs.modules.lecture.entity.Schedule;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureJdbcRepository;
import com.gjgs.gjgs.modules.lecture.repositories.participant.ParticipantJdbcRepository;
import com.gjgs.gjgs.modules.team.repositories.interfaces.TeamJdbcRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParticipantJdbcRepositoryImplTest extends SetUpLectureTeamBulletinRepository {

    @Autowired ParticipantJdbcRepository participantJdbcRepository;
    @Autowired LectureJdbcRepository lectureJdbcRepository;
    @Autowired TeamJdbcRepository teamJdbcRepository;
    @Autowired ScheduleRepository scheduleRepository;

    @Test
    @DisplayName("참가 인원 bulk insert")
    void insert_participants_test() throws Exception {

        // given
        Schedule schedule = scheduleRepository.save(ScheduleDummy.createSchedule(lecture));
        schedule.addParticipants(anotherMembers);
        participantJdbcRepository.insertParticipants(schedule);
        flushAndClear();

        // when
        List<Participant> participantList = lectureRepository.findById(lecture.getId()).orElseThrow().getSchedule(schedule.getId()).getParticipantList();

        // then
        assertEquals(participantList.size(), 3);
    }
}