package com.gjgs.gjgs.modules.coupon.repositories;

import com.gjgs.gjgs.modules.member.entity.MemberCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long> {
}
