package com.gjgs.gjgs.modules.coupon.services;

import com.gjgs.gjgs.modules.coupon.entity.Coupon;
import com.gjgs.gjgs.modules.coupon.repositories.CouponQueryRepository;
import com.gjgs.gjgs.modules.dummy.CouponDummy;
import com.gjgs.gjgs.modules.dummy.LectureDummy;
import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLecture;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DirectorMemberCouponServiceImplTest {

    @Mock SecurityUtil securityUtil;
    @Mock CouponQueryRepository couponQueryRepository;
    @InjectMocks
    DirectorCouponServiceImpl directorCouponService;

    @Test
    @DisplayName("디렉터가 운영하는 클래스의 쿠폰 정보들 가져오기")
    void get_director_coupons_test() throws Exception {

        // given
        Member director = MemberDummy.createTestDirectorMember();
        stubbingUsername(director);

        // when
        directorCouponService.getDirectorCoupons();

        // then
        assertAll(
                () -> verify(securityUtil).getCurrentUsername(),
                () -> verify(couponQueryRepository).findByDirectorUsername(director.getUsername())
        );
    }

    @Test
    @DisplayName("쿠폰 발행하기")
    void issue_coupon_test() throws Exception {

        // given
        Member director = MemberDummy.createTestDirectorMember();
        stubbingUsername(director);
        Lecture lecture = LectureDummy.createLectureWithCloseCoupon(director);
        Coupon coupon = CouponDummy.createExpiredCoupon(lecture);
        LocalDateTime previousIssueDate = coupon.getIssueDate();
        stubbingFindCouponByIdUsername(lecture.getId(), director.getUsername(), coupon);
        CreateLecture.CouponDto request = CreateLecture.CouponDto.builder().couponPrice(30).couponPrice(1000).build();

        // when
        directorCouponService.issue(lecture.getId(), request);

        // then
        assertAll(
                () -> verify(couponQueryRepository).findByLectureIdDirectorUsername(lecture.getId(), director.getUsername()),
                () -> verify(securityUtil).getCurrentUsername(),
                () -> assertEquals(coupon.getRemainCount(), request.getCouponCount()),
                () -> assertNotEquals(coupon.getIssueDate(), previousIssueDate),
                () -> assertTrue(coupon.isAvailable())
        );
    }

    @Test
    @DisplayName("쿠폰 취소하기")
    void cancel_coupon_test() throws Exception {

        // given
        Member director = MemberDummy.createTestDirectorMember();
        stubbingUsername(director);
        Lecture lecture = LectureDummy.createLectureWithCoupon(director);
        Coupon coupon = CouponDummy.createExpiredCoupon(lecture);
        stubbingFindCouponByIdUsername(lecture.getId(), director.getUsername(), coupon);

        // when
        directorCouponService.close(lecture.getId());

        // then
        assertAll(
                () -> verify(couponQueryRepository).findByLectureIdDirectorUsername(lecture.getId(), director.getUsername()),
                () -> verify(securityUtil).getCurrentUsername(),
                () -> assertFalse(coupon.isAvailable()),
                () -> assertEquals(coupon.getRemainCount(), 0)
        );
    }


    private void stubbingFindCouponByIdUsername(Long lectureId, String username, Coupon coupon) {
        when(couponQueryRepository.findByLectureIdDirectorUsername(lectureId, username)).thenReturn(Optional.of(coupon));
    }

    private void stubbingUsername(Member director) {
        when(securityUtil.getCurrentUsername()).thenReturn(Optional.of(director.getUsername()));
    }
}