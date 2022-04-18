package com.batch.redisbatch.repository.interfaces;

import com.batch.redisbatch.domain.MemberCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long> {
}
