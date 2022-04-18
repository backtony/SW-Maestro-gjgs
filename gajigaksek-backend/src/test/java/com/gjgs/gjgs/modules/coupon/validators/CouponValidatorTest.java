package com.gjgs.gjgs.modules.coupon.validators;

import com.gjgs.gjgs.modules.coupon.entity.Coupon;
import com.gjgs.gjgs.modules.exception.coupon.AlreadyHasCouponException;
import com.gjgs.gjgs.modules.exception.coupon.InvalidCouponException;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.entity.MemberCoupon;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class CouponValidatorTest {

    @InjectMocks  CouponValidator couponValidator;

    @Test
    @DisplayName("쿠폰 발급받기 / 중복 쿠폰이 있을 경우 예외 발생")
    void get_member_coupon_should_not_has_duplicate_coupon() throws Exception {

        // given
        String serialNumber = "test";
        Member member = Member.builder()
                .coupons(List.of(
                        MemberCoupon.builder().serialNumber(serialNumber).build()
                ))
                .build();
        Coupon coupon = Coupon.builder().serialNumber(serialNumber).build();

        // when, then
        assertThrows(AlreadyHasCouponException.class,
                () -> couponValidator.validateDuplicate(member, coupon),
                "중복된 쿠폰을 발급받을 수 없다.");
    }

    @Test
    @DisplayName("쿠폰 발급받기 / 사용할 수 없는 쿠폰일 경우 예외 발생")
    void get_member_coupon_should_require_coupon_is_available() throws Exception {

        // given
        Member member = Member.builder().build();
        Coupon coupon = Coupon.builder().available(false).build();

        // when, then
        assertThrows(InvalidCouponException.class,
                () -> couponValidator.validateDuplicate(member, coupon),
                "쿠폰이 활성화 되지 않은 경우 사용할 수 없다.");
    }

    @Test
    @DisplayName("쿠폰 발급받기 / 쿠폰 사용 기간이 맞지 않을 경우 예외 발생")
    void get_member_coupon_should_require_between_coupon_use_date() throws Exception {

        // given
        Member member = Member.builder().build();
        Coupon coupon = Coupon.builder().available(true)
                .issueDate(LocalDateTime.now().plusDays(1))
                .closeDate(LocalDateTime.now().plusDays(31))
                .build();

        // when, then
        assertThrows(InvalidCouponException.class,
                () -> couponValidator.validateDuplicate(member, coupon),
                "쿠폰이 활성화 되지 않은 경우 사용할 수 없다.");
    }

    @Test
    @DisplayName("쿠폰 발급받기")
    void get_member_coupon() throws Exception {

        // given
        Member member = Member.builder().build();
        Coupon coupon = Coupon.builder().available(true)
                .issueDate(LocalDateTime.now())
                .closeDate(LocalDateTime.now().plusDays(30))
                .build();

        // when, then
        assertDoesNotThrow(() -> couponValidator.validateDuplicate(member, coupon));
    }

    @Test
    @DisplayName("쿠폰 사용시, 시리얼 넘버가 맞지 않을 경우")
    void use_member_coupon_should_equal_serial_number() throws Exception {

        // given
        String serialNumber = "test";
        Member member = Member.builder()
                .coupons(List.of(
                        MemberCoupon.builder().id(1L).serialNumber(serialNumber).build()
                ))
                .build();
        Coupon coupon = Coupon.builder().available(true)
                .serialNumber("test123123123")
                .issueDate(LocalDateTime.now())
                .closeDate(LocalDateTime.now().plusDays(30))
                .build();

        // when, then
        assertThrows(InvalidCouponException.class,
                () -> couponValidator.validateAvailableMemberCoupon(member.getMemberCoupon(1L), coupon),
                "시리얼 넘버가 일치해야 한다.");
    }

    @Test
    @DisplayName("쿠폰 사용")
    void use_member_coupon() throws Exception {

        // given
        String serialNumber = "test";
        Member member = Member.builder()
                .coupons(List.of(
                        MemberCoupon.builder().id(1L).serialNumber(serialNumber).build()
                ))
                .build();
        Coupon coupon = Coupon.builder().available(true)
                .serialNumber(serialNumber)
                .issueDate(LocalDateTime.now())
                .closeDate(LocalDateTime.now().plusDays(30))
                .build();

        // when, then
        assertDoesNotThrow(() -> couponValidator.validateAvailableMemberCoupon(member.getMemberCoupon(1L), coupon));
    }
}
