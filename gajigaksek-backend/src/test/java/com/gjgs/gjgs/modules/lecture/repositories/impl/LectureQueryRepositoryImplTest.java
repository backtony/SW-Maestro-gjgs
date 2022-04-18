package com.gjgs.gjgs.modules.lecture.repositories.impl;

import com.gjgs.gjgs.config.repository.SetUpLectureTeamBulletinRepository;
import com.gjgs.gjgs.modules.category.entity.Category;
import com.gjgs.gjgs.modules.dummy.LectureDummy;
import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.dummy.ScheduleDummy;
import com.gjgs.gjgs.modules.lecture.dtos.LectureDetailResponse;
import com.gjgs.gjgs.modules.lecture.dtos.director.lecture.DirectorLectureResponse;
import com.gjgs.gjgs.modules.lecture.dtos.director.lecture.GetLectureType;
import com.gjgs.gjgs.modules.lecture.dtos.director.schedule.DirectorScheduleResponse;
import com.gjgs.gjgs.modules.lecture.dtos.search.LectureSearchResponse;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.lecture.entity.Schedule;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureJdbcRepository;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureQueryRepository;
import com.gjgs.gjgs.modules.lecture.repositories.schedule.ScheduleRepository;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.question.repository.QuestionRepository;
import com.gjgs.gjgs.modules.zone.entity.Zone;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import java.util.NoSuchElementException;
import java.util.Set;

import static com.gjgs.gjgs.modules.lecture.enums.LectureStatus.ACCEPT;
import static com.gjgs.gjgs.modules.lecture.enums.ScheduleStatus.RECRUIT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LectureQueryRepositoryImplTest extends SetUpLectureTeamBulletinRepository {

    @Autowired LectureQueryRepository lectureQueryRepository;
    @Autowired QuestionRepository questionRepository;
    @Autowired LectureJdbcRepository lectureJdbcRepository;
    @Autowired ScheduleRepository scheduleRepository;

    @AfterEach
    void tearDown() throws Exception {
        scheduleRepository.deleteAll();
        questionRepository.deleteAll();
    }

    @Test
    @DisplayName("클래스의 카테고리와 지역 한번에 가져오기")
    void find_with_category_zone_test() throws Exception {

        // given
        int count = (int) memberRepository.count();
        Member anotherDirector = memberRepository.save(MemberDummy.createDataJpaTestDirector(count + 1, zone, category));
        flushAndClear();

        // when
        Lecture findLecture = lectureQueryRepository.findWithCategoryZoneByDirectorUsername(director.getUsername()).orElseThrow();

        // then
        Zone findZone = findLecture.getZone();
        Category findCategory = findLecture.getCategory();
        String findThumbnailUrl = findLecture.getThumbnailImageFileUrl();
        assertAll(
                () -> assertEquals(findZone.getId(), zone.getId()),
                () -> assertEquals(findCategory.getId(), category.getId()),
                () -> assertEquals(findThumbnailUrl, lecture.getThumbnailImageFileUrl()),
                () -> assertThrows(NoSuchElementException.class,
                        () -> lectureQueryRepository.findWithCategoryZoneByDirectorUsername(anotherDirector.getUsername()).orElseThrow()));
    }

    @Test
    @DisplayName("클래스 상세 정보 가져오기")
    void find_with_director_question_list_test() throws Exception {

        // given
        Lecture lecture = lectureRepository.save(LectureDummy.createDataJpaTestAcceptLecture(zone, category, director));
        lectureJdbcRepository.insertSchedule(lecture);
        lectureJdbcRepository.insertCurriculum(lecture);
        lectureJdbcRepository.insertFinishedProduct(lecture);
        flushAndClear();

        // when
        LectureDetailResponse response = lectureQueryRepository.findLectureDetail(lecture.getId())
                .orElseThrow();

        // then
        Set<LectureDetailResponse.CurriculumResponse> curriculumResponseList =
                response.getCurriculumResponseList();
        Set<LectureDetailResponse.FinishedProductResponse> finishedProductResponseList =
                response.getFinishedProductResponseList();
        Set<LectureDetailResponse.ScheduleResponse> scheduleResponseList =
                response.getScheduleResponseList();
        assertAll(
                () -> assertThat(response.getLectureId()).isEqualTo(lecture.getId()),
                () -> assertThat(response.getZoneId()).isEqualTo(zone.getId()),
                () -> assertThat(response.getCategoryId()).isEqualTo(category.getId()),
                () -> assertThat(response.getDirectorResponse().getDirectorId()).isEqualTo(director.getId()),
                () -> assertThat(curriculumResponseList.size()).isEqualTo(4),
                () -> assertThat(finishedProductResponseList.size()).isEqualTo(4),
                () -> assertThat(scheduleResponseList.size()).isEqualTo(4)
        );
    }

    @Test
    @DisplayName("디렉터가 진행하는 클래스 모두 가져오기")
    void find_lectures_by_director_username_finished_test() throws Exception {

        // given
        lectureRepository.save(LectureDummy.createDataJpaTestClosedLecture(zone, category, director));
        Lecture progressLecture = lectureRepository.save(LectureDummy.createDataJpaTestProgressLecture(zone, category, director));
        lectureJdbcRepository.insertSchedule(progressLecture);
        lectureJdbcRepository.insertCurriculum(progressLecture);
        lectureJdbcRepository.insertFinishedProduct(progressLecture);
        Lecture confirmLecture = lectureRepository.save(LectureDummy.createJdbcTestConfirmLecture(zone, category, director));
        lectureJdbcRepository.insertSchedule(confirmLecture);
        lectureJdbcRepository.insertCurriculum(confirmLecture);
        lectureJdbcRepository.insertFinishedProduct(confirmLecture);

        flushAndClear();
        GetLectureType allType = GetLectureType.ALL;
        GetLectureType closedType = GetLectureType.CLOSED;
        GetLectureType progressType = GetLectureType.PROGRESS;
        GetLectureType tempType = GetLectureType.TEMP;
        GetLectureType confirmType = GetLectureType.CONFIRM;


        // when
        DirectorLectureResponse all = lectureQueryRepository.findDirectorLectures(director.getUsername(), allType).orElseThrow();
        DirectorLectureResponse closed = lectureQueryRepository.findDirectorLectures(director.getUsername(), closedType).orElseThrow();
        DirectorLectureResponse progress = lectureQueryRepository.findDirectorLectures(director.getUsername(), progressType).orElseThrow();
        DirectorLectureResponse temp = lectureQueryRepository.findDirectorLectures(director.getUsername(), tempType).orElseThrow();
        DirectorLectureResponse confirm = lectureQueryRepository.findDirectorLectures(director.getUsername(), confirmType).orElseThrow();

        // then
        assertAll(
                () -> assertEquals(all.getLectureList().size(), 4),
                () -> assertEquals(closed.getLectureList().size(), 1),
                () -> assertEquals(progress.getLectureList().size(), 1),
                () -> assertEquals(temp.getLectureList().size(), 1),
                () -> assertEquals(confirm.getLectureList().size(), 1)
        );
    }

    @Test
    @DisplayName("선택한 클래스와 스케줄들 가져오기")
    void find_accept_lecture_with_schedules_by_id_username_test() throws Exception {

        // given
        Lecture lecture = lectureRepository.save(LectureDummy.createDataJpaTestLectureWith5Schedule(1, zone, category, director));
        lectureJdbcRepository.insertSchedule(lecture);
        flushAndClear();

        // when
        Lecture withSchedule = lectureQueryRepository.findAcceptLectureWithSchedulesByIdUsername(lecture.getId(), director.getUsername()).orElseThrow();

        // then
        assertAll(
                () -> assertEquals(withSchedule.getId(), lecture.getId()),
                () -> assertEquals(withSchedule.getScheduleList().size(), 5)
        );
    }

    @Test
    @DisplayName("선택한 클래스의 스케줄 정보 가져오기")
    void find_schedules_by_lecture_id_username_test() throws Exception {

        // given
        Lecture lecture = lectureRepository.save(LectureDummy.createDataJpaTestLectureWith5Schedule(1, zone, category, director));
        lectureJdbcRepository.insertSchedule(lecture);
        flushAndClear();

        // when
        DirectorScheduleResponse response = lectureQueryRepository.findSchedulesByLectureIdUsername(lecture.getId(), director.getUsername()).orElseThrow();

        // then
        assertAll(
                () -> assertEquals(response.getScheduleList().size(), 5),
                () -> assertEquals(response.getLectureId(), lecture.getId()),
                () -> assertEquals(response.getProgressTime(), lecture.getProgressTime())
        );
    }

    @Test
    @DisplayName("지정한 클래스와 스케줄을 가져온다.")
    void find_with_schedule_by_lecture_schedule_id_test() throws Exception {

        // given
        Lecture lecture = lectureRepository.save(LectureDummy.createDataJpaTestLectureWith5Schedule(1, zone, category, director));
        Schedule schedule = scheduleRepository.save(ScheduleDummy.createSchedule(lecture));
        lectureJdbcRepository.insertSchedule(lecture);
        flushAndClear();

        // when
        Lecture findLectureWithSchedule = lectureQueryRepository
                .findWithScheduleByLectureScheduleId(lecture.getId(), schedule.getId()).orElseThrow();

        //then
        assertAll(
                () -> assertEquals(findLectureWithSchedule.getScheduleList().size(), 1),
                () -> assertEquals(findLectureWithSchedule.getScheduleList().get(0).getScheduleStatus(), RECRUIT),
                () -> assertFalse(findLectureWithSchedule.isFinished()),
                () -> assertEquals(findLectureWithSchedule.getLectureStatus(), ACCEPT)
        );
    }

    @Test
    @DisplayName("검수 완료된 디렉터의 지정된 클래스 가져오기")
    void find_by_id_director_username_test() throws Exception {

        // given
        Lecture lecture = lectureRepository.save(LectureDummy.createDataJpaTestAcceptLecture(zone, category, director));

        // when
        Lecture findLecture = lectureQueryRepository.findByIdDirectorUsername(lecture.getId(), director.getUsername()).orElseThrow();

        // then
        assertEquals(lecture.getId(), findLecture.getId());
    }

    @Test
    @DisplayName("본인이 작성한 임시 저장 클래스가 있을 경우 true 반환")
    void existCreatingLectureByUsernameTest() throws Exception {

        // when, then
        assertTrue(lectureQueryRepository.existCreatingLectureByUsername(director.getUsername()));
    }

    @Test
    @DisplayName("검수 거절된 클래스 가져오기")
    void find_reject_lecture_by_id_test() throws Exception {

        // given
        Lecture lecture = lectureRepository.save(LectureDummy.createDataJpaTestRejectLecture(zone, category, director));
        flushAndClear();

        // when
        Lecture rejectLecture = lectureQueryRepository.findRejectLectureEntityById(lecture.getId()).orElseThrow();

        // then
        assertEquals(lecture.getId(), rejectLecture.getId());
    }

    @Test
    @DisplayName("디렉터가 운영중인 클래스 가져오기")
    void find_director_lectures_by_director_id_test() throws Exception {

        // given
        lectureRepository.save(LectureDummy.createDataJpaTestAcceptLecture(zone, category, director));

        PageRequest pageRequest = PageRequest.of(0, 10);

        // when
        Slice<LectureSearchResponse> response = lectureQueryRepository.findDirectorLecturesByDirectorId(pageRequest, director.getId());

        // then
        assertEquals(response.getContent().size(), 1);
    }

    @Test
    @DisplayName("lectureId로 디렉터 정보까지 가져오기")
    void find_with_director_by_id() throws Exception {

        // given
        Lecture lecture = lectureRepository.findAll().get(0);
        Member director = lecture.getDirector();

        // when
        Lecture findLecture = lectureQueryRepository.findWithDirectorById(lecture.getId()).orElseThrow();

        // then
        assertAll(
                () -> assertEquals(findLecture.getId(), lecture.getId()),
                () -> assertNotNull(director)
        );
    }

    @Test
    @DisplayName("lecture의 평점 수정하기")
    void update_Lecture_Score() throws Exception {

        // when
        double value = 2.4d;
        long count = lectureQueryRepository.updateLectureScore(lecture.getId(), value);
        flushAndClear();

        // then
        Lecture updateLecture = lectureRepository.findById(this.lecture.getId()).orElseThrow();
        assertAll(
                () -> assertEquals(count, 1),
                () -> assertEquals(updateLecture.getScore(), value)
        );
    }

    @Test
    @DisplayName("lecture 조회수 증가하기")
    void update_view_count() throws Exception {

        // when
        lectureQueryRepository.updateViewCount(lecture.getId());
        flushAndClear();

        // then
        Lecture lecture = lectureRepository.findById(this.lecture.getId()).orElseThrow();
        assertEquals(lecture.getClickCount(), 1);
    }
}
