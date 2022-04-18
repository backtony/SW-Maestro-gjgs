package com.gjgs.gjgs.modules.coupon.services;

import com.gjgs.gjgs.modules.coupon.dto.DirectorCouponResponse;
import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLecture;

public interface DirectorCouponService {

    DirectorCouponResponse getDirectorCoupons();

    void issue(Long lectureId, CreateLecture.CouponDto couponDto);

    void close(Long lectureId);
}
