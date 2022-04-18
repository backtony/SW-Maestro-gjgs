package com.gjgs.gjgs.modules.coupon.repositories;

import com.gjgs.gjgs.config.repository.SetUpLectureTeamBulletinRepository;
import com.gjgs.gjgs.modules.coupon.dto.DirectorCouponResponse;
import com.gjgs.gjgs.modules.coupon.entity.Coupon;
import com.gjgs.gjgs.modules.dummy.CouponDummy;
import com.gjgs.gjgs.modules.dummy.LectureDummy;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;


class CouponQueryRepositoryImplTest extends SetUpLectureTeamBulletinRepository {

    @Autowired
    CouponRepository couponRepository;
    @Autowired
    CouponQueryRepository couponQueryRepository;

    @AfterEach
    void tearDown() throws Exception {
        couponRepository.deleteAll();
    }

    @Test
    @DisplayName("쿠폰 가져오기")
    void find_by_lecture_id_test() throws Exception {

        // given
        couponRepository.save(CouponDummy.createCoupon(lecture));

        // when
        Coupon findCoupon = couponQueryRepository.findByLectureId(lecture.getId()).orElseThrow();

        // then
        assertAll(
                () -> assertTrue(findCoupon.isAvailable()),
                () -> assertEquals(findCoupon.getLecture().getId(), lecture.getId())
        );
    }

    @Test
    @DisplayName("검수 완료된 디렉터의 클래스들 쿠폰정보 가져오기")
    void find_with_coupon_by_director_username_test() throws Exception {

        // given
        couponRepository.save(CouponDummy.createCoupon(lecture));
        Lecture lecture2 = lectureRepository.save(LectureDummy.createDataJpaTestAcceptLecture(zone, category, director));
        couponRepository.save(CouponDummy.createCoupon(lecture2));
        flushAndClear();

        // when
        DirectorCouponResponse response = couponQueryRepository.findByDirectorUsername(director.getUsername());

        // then
        assertEquals(response.getCouponResponseList().size(), 1);
    }

    @Test
    @DisplayName("쿠폰과 연관된 클래스 정보 함께 가져오기")
    void find_with_lecture_by_lecture_id() throws Exception {

        // given
        couponRepository.save(CouponDummy.createCoupon(lecture));
        flushAndClear();

        // when
        Coupon coupon = couponQueryRepository.findWithLectureByLectureId(lecture.getId()).orElseThrow();

        // then
        assertEquals(coupon.getLecture().getId(), lecture.getId());
    }
}