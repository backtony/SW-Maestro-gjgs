package com.gjgs.gjgs.modules.lecture.services.director.schedule;

import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLecture;
import com.gjgs.gjgs.modules.lecture.dtos.director.schedule.DirectorScheduleResponse;

public interface DirectorScheduleService {

    DirectorScheduleResponse.PostDelete createSchedule(Long lectureId, CreateLecture.ScheduleDto scheduleDto);

    DirectorScheduleResponse.PostDelete deleteSchedule(Long lectureId, Long scheduleId);

    DirectorScheduleResponse getLectureSchedules(Long lectureId);
}
