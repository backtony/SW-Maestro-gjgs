package com.gjgs.gjgs.modules.lecture.services.temporaryStore.put;

import com.gjgs.gjgs.modules.dummy.LectureDummy;
import com.gjgs.gjgs.modules.exception.lecture.TemporaryNotSaveLectureException;
import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLecture;
import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLectureProcessResponse;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureJdbcRepository;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureQueryRepository;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.enums.Authority;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.gjgs.gjgs.modules.lecture.dtos.create.CreateLectureStep.SCHEDULE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PutScheduleServiceImplTest {

    @Mock SecurityUtil securityUtil;
    @Mock LectureQueryRepository lectureQueryRepository;
    @Mock LectureJdbcRepository lectureJdbcRepository;
    @InjectMocks
    PutScheduleServiceImpl putScheduleService;

    @Test
    @DisplayName("스케줄 저장하기")
    void put_lecture_schedule_test() throws Exception {

        // given
        Member director = createDirector();
        CreateLecture.ScheduleRequest request = createScheduleRequest();
        List<MultipartFile> files = getMockMultipartFileList(1);
        stubbingCurrentUsername(director);
        Lecture lecture = LectureDummy.createHaveTwoScheduleThreeCurriculumFourFinishedProduct(1, director);
        stubbingFindLecture(director, lecture);

        // when
        CreateLectureProcessResponse res = putScheduleService.putLectureProcess(request, files);

        // then
        assertAll(
                () -> verify(lectureQueryRepository).findWithSchedulesByDirectorUsername(director.getUsername()),
                () -> verify(lectureJdbcRepository).insertSchedule(lecture),
                ()-> verify(lectureQueryRepository).deleteSchedulesByLectureId(lecture.getId()),
                () -> assertEquals(res.getCompletedStepName(), SCHEDULE.name())
        );
    }

    @Test
    @DisplayName("저장된 lecture가 없을 경우 예외 발생")
    void put_lecture_schedule_should_exist_lecture_test() throws Exception {

        // given
        Member director = createDirector();
        CreateLecture.ScheduleRequest request = createScheduleRequest();
        stubbingCurrentUsername(director);
        stubbingFindLectureIsEmpty(director);

        // when, then
        assertThrows(TemporaryNotSaveLectureException.class,
                () -> putScheduleService.putLectureProcess(request, getMockMultipartFileList(1)),
                "저장된 Lecture 가 있어야 한다.");
    }

    private CreateLecture.ScheduleRequest createScheduleRequest() {
        return CreateLecture.ScheduleRequest.builder()
                .minParticipants(1).maxParticipants(6)
                .createLectureStep(SCHEDULE)
                .scheduleList(List.of(
                        CreateLecture.ScheduleDto.builder().lectureDate(LocalDate.of(2021, 8, 21)).startHour(13).startMinute(30).progressMinute(120).build(),
                        CreateLecture.ScheduleDto.builder().lectureDate(LocalDate.of(2021, 8, 22)).startHour(14).startMinute(30).progressMinute(120).build(),
                        CreateLecture.ScheduleDto.builder().lectureDate(LocalDate.of(2021, 8, 23)).startHour(15).startMinute(30).progressMinute(120).build()
                ))
                .build();
    }

    private void stubbingFindLecture(Member director, Lecture lecture) {
        when(lectureQueryRepository.findWithSchedulesByDirectorUsername(director.getUsername())).thenReturn(Optional.of(lecture));
    }

    private void stubbingFindLectureIsEmpty(Member director) {
        when(lectureQueryRepository.findWithSchedulesByDirectorUsername(director.getUsername())).thenReturn(Optional.empty());
    }

    private void stubbingCurrentUsername(Member director) {
        when(securityUtil.getCurrentUsername()).thenReturn(Optional.of(director.getUsername()));
    }

    private Member createDirector() {
        return Member.builder()
                .id(1L).authority(Authority.ROLE_DIRECTOR).username("director")
                .build();
    }

    private List<MultipartFile> getMockMultipartFileList(int count) {
        List<MultipartFile> files = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            files.add(getMockMultipartFile());
        }
        return files;
    }

    private MultipartFile getMockMultipartFile() {
        return new MockMultipartFile("files",
                "image.img",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                "image".getBytes());
    }
}