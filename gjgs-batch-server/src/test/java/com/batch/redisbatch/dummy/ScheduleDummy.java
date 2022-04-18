package com.batch.redisbatch.dummy;

import com.batch.redisbatch.domain.lecture.Lecture;
import com.batch.redisbatch.domain.lecture.Schedule;

import java.time.LocalTime;

import static com.batch.redisbatch.domain.lecture.ScheduleStatus.RECRUIT;
import static java.time.LocalDate.now;

public class ScheduleDummy {

    public static Schedule createSchedule(Lecture lecture) {
        return Schedule.builder()
                .lecture(lecture)
                .lectureDate(now())
                .startTime(LocalTime.now())
                .endTime(LocalTime.now().plusMinutes(120))
                .progressMinutes(120)
                .scheduleStatus(RECRUIT)
                .currentParticipants(0)
                .build();
    }
}
