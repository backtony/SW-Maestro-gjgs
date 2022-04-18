package com.gjgs.gjgs.modules.coupon.services;

import com.gjgs.gjgs.modules.coupon.dto.DirectorCouponResponse;
import com.gjgs.gjgs.modules.coupon.entity.Coupon;
import com.gjgs.gjgs.modules.coupon.repositories.CouponQueryRepository;
import com.gjgs.gjgs.modules.exception.coupon.CouponNotFoundException;
import com.gjgs.gjgs.modules.exception.member.MemberNotFoundException;
import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLecture;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DirectorCouponServiceImpl implements DirectorCouponService {

    private final SecurityUtil securityUtil;
    private final CouponQueryRepository couponQueryRepository;

    @Override
    @Transactional(readOnly = true)
    public DirectorCouponResponse getDirectorCoupons() {
        return couponQueryRepository.findByDirectorUsername(getDirectorUsername());
    }

    @Override
    public void issue(Long lectureId, CreateLecture.CouponDto couponDto) {
        String directorUsername = getDirectorUsername();
        Coupon coupon = findCoupon(lectureId, directorUsername);
        coupon.issueCoupon(couponDto);
    }

    @Override
    public void close(Long lectureId) {
        String directorUsername = getDirectorUsername();
        Coupon coupon = findCoupon(lectureId, directorUsername);
        coupon.closeCoupon();
    }

    private String getDirectorUsername() {
        return securityUtil.getCurrentUsername().orElseThrow(() -> new MemberNotFoundException());
    }

    private Coupon findCoupon(Long lectureId, String username) {
        return couponQueryRepository.findByLectureIdDirectorUsername(lectureId, username)
                .orElseThrow(() -> new CouponNotFoundException());
    }
}
