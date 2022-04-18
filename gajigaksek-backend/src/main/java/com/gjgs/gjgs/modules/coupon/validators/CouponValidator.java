package com.gjgs.gjgs.modules.coupon.validators;

import com.gjgs.gjgs.modules.coupon.entity.Coupon;
import com.gjgs.gjgs.modules.exception.coupon.AlreadyHasCouponException;
import com.gjgs.gjgs.modules.exception.coupon.InvalidCouponException;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.entity.MemberCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CouponValidator {

    public void validateDuplicate(Member couponReceiver, Coupon coupon) {
        checkMemberHasCoupon(couponReceiver, coupon.getSerialNumber());
        checkCouponAvailable(coupon);
    }

    private void checkMemberHasCoupon(Member couponReceiver, String serialNumber) {
        couponReceiver.getCoupons().forEach(memberCoupon -> {
            if (memberCoupon.getSerialNumber().equals(serialNumber)) {
                throw new AlreadyHasCouponException();
            }
        });
    }

    public void validateAvailableMemberCoupon(MemberCoupon memberCoupon, Coupon coupon) {
        checkCouponAvailable(coupon);
        checkSerialNumber(memberCoupon, coupon);
    }

    private void checkCouponAvailable(Coupon coupon) {
        if (!coupon.isAvailable()) {
            throw new InvalidCouponException();
        }
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(coupon.getIssueDate()) || now.isAfter(coupon.getCloseDate())) {
            throw new InvalidCouponException();
        }
    }

    private void checkSerialNumber(MemberCoupon memberCoupon, Coupon coupon) {
        if (!memberCoupon.getSerialNumber().equals(coupon.getSerialNumber())) {
            throw new InvalidCouponException();
        }
    }
}
