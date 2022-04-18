package com.gjgs.gjgs.modules.coupon.entity;

import com.gjgs.gjgs.modules.exception.coupon.AlreadyUsedCouponException;
import com.gjgs.gjgs.modules.member.entity.MemberCoupon;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberCouponTest {

    @Test
    @DisplayName("할인 쿠폰 적용 / 이미 쿠폰을 사용한 경우 예외 발생")
    void use_test_should_target_not_used() throws Exception {

        // given
        MemberCoupon coupon = MemberCoupon.builder().used(true).id(1L).discountPrice(1000).build();

        // when, then
        assertThrows(AlreadyUsedCouponException.class,
                coupon::use,
                "사용하지 않은 쿠폰만 사용할 수 있다.");
    }

    @Test
    @DisplayName("할인 쿠폰 적용")
    void use_test_invalid_total_price() throws Exception {

        // given
        MemberCoupon coupon = MemberCoupon.builder().used(false).id(1L).discountPrice(1000).build();

        // when
        int discountPrice = coupon.use();

        // then
        assertAll(
                () -> assertEquals(discountPrice, coupon.getDiscountPrice()),
                () -> assertTrue(coupon.isUsed())
        );
    }
}
