package com.gjgs.gjgs.modules.lecture.services.director.schedule;

import com.gjgs.gjgs.modules.dummy.LectureDummy;
import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLecture;
import com.gjgs.gjgs.modules.lecture.dtos.director.schedule.DirectorScheduleResponse;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.lecture.entity.Schedule;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureQueryRepository;
import com.gjgs.gjgs.modules.lecture.repositories.schedule.ScheduleRepository;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static com.gjgs.gjgs.modules.lecture.dtos.director.schedule.DirectorScheduleResponse.PostDelete.Result.CREATE;
import static com.gjgs.gjgs.modules.lecture.dtos.director.schedule.DirectorScheduleResponse.PostDelete.Result.DELETE;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DirectorScheduleServiceImplTest {

    @Mock SecurityUtil securityUtil;
    @Mock ScheduleRepository scheduleRepository;
    @Mock LectureQueryRepository lectureQueryRepository;
    @InjectMocks
    DirectorScheduleServiceImpl directorScheduleService;

    @Test
    @DisplayName("스케줄 저장하기")
    void create_schedule_test() throws Exception {

        // given
        CreateLecture.ScheduleDto request = createScheduleDto();
        Member director = MemberDummy.createTestDirectorMember();
        stubbingCurrentUsername(director);
        Lecture lecture = LectureDummy.createLecture(1, director);
        stubbingFindLecture(lecture, director);
        Schedule schedule = Schedule.of(request, lecture);
        stubbingSaveSchedule(schedule);

        // when
        DirectorScheduleResponse.PostDelete response = directorScheduleService.createSchedule(lecture.getId(), request);

        // then
        assertAll(
                () -> verify(securityUtil).getCurrentUsername(),
                () -> verify(scheduleRepository).save(any()),
                () -> verify(lectureQueryRepository).findAcceptLectureWithSchedulesByIdUsername(lecture.getId(), director.getUsername()),
                () -> assertEquals(response.getResult(), CREATE),
                () -> assertEquals(lecture.getScheduleList().size(), 3)
        );
    }

    @Test
    @DisplayName("스케줄 삭제하기")
    void delete_schedule_test() throws Exception {

        // given
        Member director = MemberDummy.createTestDirectorMember();
        stubbingCurrentUsername(director);
        Lecture lecture = LectureDummy.createLectureWithSixSchedule(1, director);
        stubbingFindLecture(lecture, director);
        Schedule deleteSchedule = lecture.getSchedule(5L);

        // when
        DirectorScheduleResponse.PostDelete response = directorScheduleService.deleteSchedule(lecture.getId(), deleteSchedule.getId());

        // then
        assertAll(
                () -> verify(securityUtil).getCurrentUsername(),
                () -> verify(lectureQueryRepository).findAcceptLectureWithSchedulesByIdUsername(lecture.getId(), director.getUsername()),
                () -> verify(scheduleRepository).delete(deleteSchedule),
                () -> assertEquals(response.getResult(), DELETE),
                () -> assertEquals(lecture.getScheduleList().size(), 5)
        );
    }

    private void stubbingSaveSchedule(Schedule schedule) {
        when(scheduleRepository.save(schedule)).thenReturn(schedule);
    }

    private CreateLecture.ScheduleDto createScheduleDto() {
        return CreateLecture.ScheduleDto.builder()
                .lectureDate(LocalDate.now())
                .startHour(12)
                .startMinute(0)
                .progressMinute(120)
                .build();
    }

    private void stubbingCurrentUsername(Member director) {
        when(securityUtil.getCurrentUsername()).thenReturn(Optional.of(director.getUsername()));
    }

    private void stubbingFindLecture(Lecture lecture, Member director) {
        when(lectureQueryRepository.findAcceptLectureWithSchedulesByIdUsername(lecture.getId(), director.getUsername()))
                .thenReturn(Optional.of(lecture));
    }
}