package com.gjgs.gjgs.modules.dummy;

import com.gjgs.gjgs.modules.coupon.entity.Coupon;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.time.LocalDateTime.now;

public class CouponDummy {

    public static Coupon createCoupon(Lecture lecture) {
        return Coupon.builder().discountPrice(1000)
                .serialNumber(UUID.randomUUID().toString())
                .chargeCount(20).remainCount(1).issueDate(now()).closeDate(now().plusDays(30))
                .lecture(lecture).receivePeople(0).title("쿠폰 테스트")
                .available(true)
                .build();
    }

    public static Coupon createExpiredCoupon(Lecture lecture) {
        return Coupon.builder().discountPrice(1000)
                .serialNumber(UUID.randomUUID().toString())
                .chargeCount(20).remainCount(0).issueDate(now()).closeDate(now().plusDays(30))
                .lecture(lecture).receivePeople(0).title("쿠폰 테스트")
                .available(false)
                .build();
    }

    public static Coupon createAllLectureEnableCoupon() {
        return Coupon.builder()
                .serialNumber(UUID.randomUUID().toString())
                .discountPrice(1000)
                .title("백오피스에서 뿌리는 쿠폰")
                .remainCount(100)
                .available(true)
                .issueDate(LocalDateTime.now())
                .closeDate(LocalDateTime.now().plusDays(30))
                .receivePeople(0)
                .build();
    }
}
