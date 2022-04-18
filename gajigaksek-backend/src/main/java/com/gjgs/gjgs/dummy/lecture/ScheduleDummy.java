package com.gjgs.gjgs.dummy.lecture;

import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLecture;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static java.time.LocalDate.now;


@Component
@RequiredArgsConstructor
@Profile("dev")
public class ScheduleDummy {
    public CreateLecture.ScheduleRequest createScheduleList() {

        LocalDate today = now();

        return CreateLecture.ScheduleRequest.builder()
                .minParticipants(1).maxParticipants(6)
                .scheduleList(List.of(
                CreateLecture.ScheduleDto.builder()
                        .progressMinute(120)
                        .lectureDate(today.plusDays(2L))
                        .startHour(16).startMinute(30)
                        .endHour(18).endMinute(30)
                        .build(),
                CreateLecture.ScheduleDto.builder()
                        .progressMinute(120)
                        .lectureDate(today.plusDays(2L))
                        .startHour(13).startMinute(30)
                        .endHour(15).endMinute(30)
                        .build(),
                CreateLecture.ScheduleDto.builder()
                        .progressMinute(120)
                        .lectureDate(today.plusDays(1L))
                        .startHour(12).startMinute(30)
                        .endHour(14).endMinute(30)
                        .build(),
                CreateLecture.ScheduleDto.builder()
                        .progressMinute(120)
                        .lectureDate(today.plusDays(1L))
                        .startHour(12).startMinute(30)
                        .endHour(14).endMinute(30)
                        .build())).build();
    }
}
