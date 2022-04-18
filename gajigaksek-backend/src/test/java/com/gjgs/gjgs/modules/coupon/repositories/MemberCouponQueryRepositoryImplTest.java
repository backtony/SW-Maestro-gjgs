package com.gjgs.gjgs.modules.coupon.repositories;

import com.gjgs.gjgs.config.repository.SetUpLectureTeamBulletinRepository;
import com.gjgs.gjgs.modules.coupon.dto.EnableMemberCouponResponse;
import com.gjgs.gjgs.modules.coupon.entity.Coupon;
import com.gjgs.gjgs.modules.dummy.CouponDummy;
import com.gjgs.gjgs.modules.dummy.LectureDummy;
import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.member.dto.mypage.MyAvailableCouponResponse;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.entity.MemberCoupon;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MemberCouponQueryRepositoryImplTest extends SetUpLectureTeamBulletinRepository {

    @Autowired
    CouponRepository couponRepository;
    @Autowired
    MemberCouponQueryRepository memberCouponQueryRepository;
    @Autowired
    MemberCouponRepository memberCouponRepository;

    @AfterEach
    void tearDown() throws Exception {
        memberCouponRepository.deleteAll();
        couponRepository.deleteAll();
    }

    @Test
    @DisplayName("쿠폰을 소유한 회원 정보 가져오기 / 여러 클래스에 대한 쿠폰을 받아도 한장만 존재")
    void find_with_coupon_lecture_by_member_coupon_id_test() throws Exception {

        // given
        Coupon coupon1 = couponRepository.save(CouponDummy.createCoupon(lecture));
        Lecture lecture2 = lectureRepository.save(LectureDummy.createJdbcTestConfirmLecture(zone, category, director));
        Coupon coupon2 = couponRepository.save(CouponDummy.createCoupon(lecture2));

        Member couponReceiver = memberRepository.save(MemberDummy.createDataJpaTestDirector(2, zone, category));
        MemberCoupon memberCoupon = memberCouponRepository.save(MemberCoupon.of(couponReceiver, coupon1.getDiscountPrice(), coupon1.getSerialNumber()));
        memberCouponRepository.save(MemberCoupon.of(couponReceiver, coupon2.getDiscountPrice(), coupon2.getSerialNumber()));
        flushAndClear();

        // when
        Member findMember = memberCouponQueryRepository
                .findByMemberCouponByIdUsername(memberCoupon.getId(), couponReceiver.getUsername())
                .orElseThrow();

        // then
        assertEquals(findMember.getCoupons().size(), 1);
    }

    @Test
    @DisplayName("결제 페이지에서 해당 클래스에 적용 가능한 쿠폰 가져오기")
    void find_my_enable_coupon_by_lecture_id_test() throws Exception {

        // given
        Coupon coupon1 = couponRepository.save(CouponDummy.createCoupon(lecture));
        Coupon coupon2 = couponRepository.save(CouponDummy.createAllLectureEnableCoupon());

        Member couponReceiver = memberRepository.save(MemberDummy.createDataJpaTestDirector(2, zone, category));
        memberCouponRepository.save(MemberCoupon.of(couponReceiver, coupon1.getDiscountPrice(), coupon1.getSerialNumber()));
        memberCouponRepository.save(MemberCoupon.of(couponReceiver, coupon2.getDiscountPrice(), coupon2.getSerialNumber()));
        flushAndClear();

        // when
        EnableMemberCouponResponse response = memberCouponQueryRepository
                .findMyEnableCouponByLectureIdUsername(lecture.getId(), couponReceiver.getUsername());

        // then
        assertEquals(response.getEnableCouponList().size(), 2);
    }

    @Test
    @DisplayName("마이페이지에서 내가 발급받은 전체 쿠폰 가져오기")
    void find_my_available_coupons_by_username_test() throws Exception {

        // given
        Coupon coupon1 = couponRepository.save(CouponDummy.createCoupon(lecture));
        Coupon coupon2 = couponRepository.save(CouponDummy.createAllLectureEnableCoupon());

        Member couponReceiver = memberRepository.save(MemberDummy.createDataJpaTestDirector(2, zone, category));
        memberCouponRepository.save(MemberCoupon.of(couponReceiver, coupon1.getDiscountPrice(), coupon1.getSerialNumber()));
        memberCouponRepository.save(MemberCoupon.of(couponReceiver, coupon2.getDiscountPrice(), coupon2.getSerialNumber()));
        flushAndClear();

        // when
        MyAvailableCouponResponse response = memberCouponQueryRepository
                .findMyAvailableCouponsByUsername(couponReceiver.getUsername());

        // then
        assertEquals(response.getMyCouponList().size(), 2);
    }
}
