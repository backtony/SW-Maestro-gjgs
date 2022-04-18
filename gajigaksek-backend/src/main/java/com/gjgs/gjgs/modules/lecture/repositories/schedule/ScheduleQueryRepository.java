package com.gjgs.gjgs.modules.lecture.repositories.schedule;

import com.gjgs.gjgs.modules.lecture.entity.Schedule;

import java.util.Optional;

public interface ScheduleQueryRepository {

    Optional<Schedule> findWithParticipantsById(Long scheduleId);

    Optional<Schedule> findWithLectureParticipantsByLectureScheduleId(Long lectureId, Long scheduleId);
}
