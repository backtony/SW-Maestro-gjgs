package com.gjgs.gjgs.modules.coupon.services;

import com.gjgs.gjgs.modules.coupon.dto.EnableMemberCouponResponse;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.payment.dto.PaymentRequest;

public interface MemberCouponService {

    void giveMemberCoupon(Long lectureId);

    Member getMemberOrWithCoupon(PaymentRequest paymentRequest);

    EnableMemberCouponResponse getMemberCoupon(Long lectureId);
}
