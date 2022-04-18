package com.gjgs.gjgs.modules.coupon.repositories;

import com.gjgs.gjgs.modules.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
