package com.gjgs.gjgs.modules.lecture.repositories.temporaryStore;

import com.gjgs.gjgs.config.repository.SetUpMemberRepository;
import com.gjgs.gjgs.modules.coupon.entity.Coupon;
import com.gjgs.gjgs.modules.coupon.repositories.CouponRepository;
import com.gjgs.gjgs.modules.dummy.CouponDummy;
import com.gjgs.gjgs.modules.dummy.LectureDummy;
import com.gjgs.gjgs.modules.lecture.dtos.admin.ConfirmLectureResponse;
import com.gjgs.gjgs.modules.lecture.dtos.create.PutLectureResponse;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureJdbcRepository;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TemporaryStoredLectureQueryRepositoryImplTest extends SetUpMemberRepository {

    @Autowired TemporaryStoredLectureQueryRepository tempLectureQueryRepository;
    @Autowired LectureRepository lectureRepository;
    @Autowired CouponRepository couponRepository;
    @Autowired LectureJdbcRepository lectureJdbcRepository;

    @AfterEach
    void tearDown() throws Exception {
        couponRepository.deleteAll();
        lectureRepository.deleteAll();
    }

    @Test
    @DisplayName("임시 저장한 클래스 가져오기 / 스케줄, 완성작, 커리큘럼이 없으면 리스트 크기는 1개")
    void find_by_director_username_test() throws Exception {

        // given
        Lecture lecture = lectureRepository.save(LectureDummy.createDataJpaTestLecture(zone, category, director));
        Coupon coupon = couponRepository.save(CouponDummy.createCoupon(lecture));
        flushAndClear();

        // when
        PutLectureResponse res = tempLectureQueryRepository.findByDirectorUsername(director.getUsername()).orElseThrow();

        // then
        assertAll(
                () -> assertEquals(res.getLectureId(), lecture.getId()),
                () -> assertEquals(res.getFinishedProductList().size(), 1),
                () -> assertEquals(res.getCurriculumList().size(), 1),
                () -> assertEquals(res.getScheduleList().size(), 1),
                () -> assertEquals(res.getCoupon().getCouponCount(), coupon.getChargeCount())
        );
    }

    @Test
    @DisplayName("검수를 받아야하는 클래스 가져오기 (페이징)")
    void find_confirm_lectures_test() {

        // given
        lectureRepository.save(LectureDummy.createJdbcTestConfirmLecture(zone, category, director));
        flushAndClear();

        PageRequest pageRequest = PageRequest.of(0, 10);

        // when
        Page<ConfirmLectureResponse> response = tempLectureQueryRepository.findConfirmLectures(pageRequest);

        // then
        assertEquals(response.getContent().size(), 1);
    }

    @Test
    @DisplayName("검수를 받아야 할 클래스 상세 조회하기")
    void find_confirm_lecture_by_id_test() throws Exception {

        // given
        Lecture lecture = lectureRepository.save(LectureDummy.createJdbcTestConfirmLecture(zone, category, director));
        lectureJdbcRepository.insertSchedule(lecture);
        lectureJdbcRepository.insertCurriculum(lecture);
        lectureJdbcRepository.insertFinishedProduct(lecture);
        flushAndClear();

        // when
        PutLectureResponse response = tempLectureQueryRepository.findConfirmLectureById(lecture.getId()).orElseThrow();

        // then
        assertEquals(response.getLectureId(), lecture.getId());
    }
}