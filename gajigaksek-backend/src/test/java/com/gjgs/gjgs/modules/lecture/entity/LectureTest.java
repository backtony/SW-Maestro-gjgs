package com.gjgs.gjgs.modules.lecture.entity;

import com.gjgs.gjgs.modules.category.entity.Category;
import com.gjgs.gjgs.modules.dummy.LectureDummy;
import com.gjgs.gjgs.modules.exception.lecture.InvalidActionCauseIAmDirectorException;
import com.gjgs.gjgs.modules.exception.schedule.ScheduleNotFoundException;
import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLecture;
import com.gjgs.gjgs.modules.lecture.enums.LectureStatus;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.enums.Authority;
import com.gjgs.gjgs.modules.utils.vo.FileInfoVo;
import com.gjgs.gjgs.modules.zone.entity.Zone;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LectureTest {

    @Test
    @DisplayName("문의자가 자신이 만든 클래스에 문의할 수 없다.")
    void questioner_should_not_own_lecture() throws Exception {

        // given
        Member director = Member.builder().id(13L).username("test").build();
        Lecture lecture = LectureDummy.createLecture(1, director);
        Member questionerEqualsDirector = Member.builder()
                .id(director.getId()).build();

        // when, then
        assertThrows(InvalidActionCauseIAmDirectorException.class,
                () -> lecture.checkNotDirector(lecture.getDirector(), questionerEqualsDirector),
                "문의자가 해당 클래스 개설자일 경우 문의할 수 없다.");
    }

    @Test
    @DisplayName("of 메서드 테스트")
    void of_test() throws Exception {

        // given
        Member director = Member.builder()
                .id(1L).username("director").authority(Authority.ROLE_DIRECTOR)
                .build();
        Category category = Category.builder().id(2L).mainCategory("main").subCategory("sub").build();
        Zone zone = Zone.builder().id(2L).mainZone("main").subZone("sub").build();
        String fileName = "fileName";
        String fileUrl = "fileUrl";
        FileInfoVo fileInfoVo = FileInfoVo.of(fileName, fileUrl);
        CreateLecture.FirstRequest firstRequest = CreateLecture.FirstRequest
                .builder()
                .categoryId(category.getId()).title("title").address("address")
                .build();

        // when
        Lecture lecture = Lecture.of(List.of(fileInfoVo), category, zone, firstRequest, director);

        // then
        assertAll(
                () -> assertEquals(lecture.getCategory(), category),
                () -> assertEquals(lecture.getThumbnailImageFileName(), fileName),
                () -> assertEquals(lecture.getThumbnailImageFileUrl(), fileUrl),
                () -> assertEquals(lecture.getTitle(), firstRequest.getTitle()),
                () -> assertEquals(lecture.getFullAddress(), firstRequest.getAddress()),
                () -> assertEquals(lecture.getLectureStatus(), LectureStatus.CREATING)
        );
    }

    @Test
    @DisplayName("클래스 소개(intro) 저장하기")
    void set_intro_test() throws Exception {

        // given
        CreateLecture.IntroRequest introRequest = CreateLecture.IntroRequest.builder()
                .mainText("test")
                .finishedProductInfoList(List.of(
                        CreateLecture.FinishedProductInfoDto.builder().order(0).text("test").build(),
                        CreateLecture.FinishedProductInfoDto.builder().order(1).text("test").build()
                ))
                .build();
        List<FileInfoVo> finishedProductImageInfoList = List.of(
                FileInfoVo.builder().fileName("name1").fileUrl("url2").build(),
                FileInfoVo.builder().fileName("name2").fileUrl("url2").build()
        );
        Lecture lecture = Lecture.builder().build();

        // when
        lecture.putIntro(introRequest, finishedProductImageInfoList);

        // then
        assertAll(
                () -> assertEquals(lecture.getMainText(), introRequest.getMainText()),
                () -> assertEquals(lecture.getFinishedProductList().size(), 2)
        );
    }

    @Test
    @DisplayName("스케줄 추가하기")
    void add_schedule_test() throws Exception {

        // given
        Lecture lecture = Lecture.builder().id(1L).build();
        Schedule schedule = Schedule.builder().id(2L).build();

        // when
        lecture.addSchedule(schedule);

        // then
        assertEquals(lecture.getScheduleList().size(), 1);
    }

    @Test
    @DisplayName("스케줄 ID로 가져오기")
    void get_schedule_test() throws Exception {

        // given
        Lecture lecture = Lecture.builder().id(1L)
                .scheduleList(new ArrayList<>(List.of(
                        Schedule.builder().id(123L).build()
                )))
                .build();

        // when
        Schedule schedule = lecture.getSchedule(123L);

        // then
        assertAll(
                () -> assertNotNull(schedule),
                () -> assertEquals(schedule.getId(), lecture.getScheduleList().get(0).getId())
        );
    }

    @Test
    @DisplayName("스케줄 ID로 가져오기 / 없을 경우 예외 발생")
    void get_schedule_not_found_exception_test() throws Exception {

        // given
        Lecture lecture = Lecture.builder().id(1L)
                .scheduleList(new ArrayList<>(List.of(
                        Schedule.builder().id(123L).build()
                )))
                .build();

        // when
        assertThrows(ScheduleNotFoundException.class,
                () -> lecture.getSchedule(456L),
                "스케줄이 없을 경우 예외 발생");
    }

    @Test
    @DisplayName("스케줄 지우기")
    void remove_schedule_test() throws Exception {

        // given
        Lecture lecture = Lecture.builder().id(1L)
                .scheduleList(new ArrayList<>(List.of(
                        Schedule.builder().id(123L).build()
                )))
                .build();
        Schedule schedule = lecture.getSchedule(123L);

        // when
        lecture.removeSchedule(schedule);

        // then
        assertEquals(lecture.getScheduleList().size(), 0);
    }
}
