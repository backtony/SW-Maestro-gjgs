package com.batch.redisbatch.repository.interfaces;

import com.batch.redisbatch.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
