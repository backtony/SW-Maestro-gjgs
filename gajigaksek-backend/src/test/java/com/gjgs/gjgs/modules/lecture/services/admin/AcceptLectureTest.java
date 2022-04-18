package com.gjgs.gjgs.modules.lecture.services.admin;

import com.gjgs.gjgs.modules.coupon.entity.Coupon;
import com.gjgs.gjgs.modules.coupon.repositories.CouponQueryRepository;
import com.gjgs.gjgs.modules.dummy.CouponDummy;
import com.gjgs.gjgs.modules.dummy.LectureDummy;
import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.lecture.dtos.admin.RejectReason;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.lecture.enums.LectureStatus;
import com.gjgs.gjgs.modules.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AcceptLectureTest {

    @Mock CouponQueryRepository couponQueryRepository;
    @InjectMocks AcceptLecture acceptLecture;

    @Test
    @DisplayName("클래스 검수 수락하기")
    void accept_lecture_test()throws Exception {

        // given
        Member director = MemberDummy.createTestDirectorMember();
        Lecture lecture = LectureDummy.createLecture(1, director);
        Coupon coupon = CouponDummy.createCoupon(lecture);
        stubbingFindCouponLecture(lecture, coupon);

        // when
        acceptLecture.decide(lecture.getId(), RejectReason.builder().build());

        // then
        assertAll(
                () -> verify(couponQueryRepository).findWithLectureByLectureId(lecture.getId()),
                () -> assertEquals(coupon.getLecture().getLectureStatus(), LectureStatus.ACCEPT),
                () -> assertTrue(coupon.isAvailable()),
                () -> assertNotNull(coupon.getIssueDate()),
                () -> assertNotNull(coupon.getCloseDate())
        );
    }

    private void stubbingFindCouponLecture(Lecture lecture, Coupon coupon) {
        when(couponQueryRepository.findWithLectureByLectureId(lecture.getId())).thenReturn(Optional.of(coupon));
    }
}