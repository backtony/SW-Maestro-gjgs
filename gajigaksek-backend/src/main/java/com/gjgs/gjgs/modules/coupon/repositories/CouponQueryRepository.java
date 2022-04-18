package com.gjgs.gjgs.modules.coupon.repositories;

import com.gjgs.gjgs.modules.coupon.dto.DirectorCouponResponse;
import com.gjgs.gjgs.modules.coupon.entity.Coupon;

import java.util.Optional;

public interface CouponQueryRepository {

    Optional<Coupon> findByLectureId(Long lectureId);

    Optional<Coupon> findByLectureIdDirectorUsername(Long lectureId, String username);

    DirectorCouponResponse findByDirectorUsername(String username);

    Optional<Coupon> findWithLectureByLectureId(Long lectureId);
}
