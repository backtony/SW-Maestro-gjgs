package com.gjgs.gjgs.dummy.lecture;

import com.gjgs.gjgs.modules.coupon.entity.Coupon;
import com.gjgs.gjgs.modules.coupon.repositories.CouponRepository;
import com.gjgs.gjgs.modules.coupon.repositories.MemberCouponRepository;
import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLecture;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.entity.MemberCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static java.util.stream.Collectors.toList;


@Component
@RequiredArgsConstructor
@Profile("dev")
public class CouponDummy {

    private final CouponRepository couponRepository;
    private final MemberCouponRepository memberCouponRepository;

    public void createAcceptLectureCoupon(Lecture lecture) {
        StringBuffer buffer = new StringBuffer();
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        couponRepository.save(Coupon.builder()
                .lecture(lecture)
                .serialNumber(UUID.randomUUID().toString())
                .discountPrice(2000)
                .chargeCount(20)
                .remainCount(20)
                .receivePeople(0)
                .available(true)
                .title(buffer.append(lecture.getTitle())
                        .append(" ").append(decimalFormat.format(2000))
                        .append(" 원 할인 쿠폰")
                        .toString())
                .issueDate(now())
                .closeDate(now().plusDays(30L))
                .build());
    }

    private CreateLecture.CouponDto getCouponDto() {
        return CreateLecture.CouponDto.builder()
                .couponPrice(1000).couponCount(10)
                .build();
    }

    public void giveCoupon(Member leader) {
        List<Coupon> allCoupons = couponRepository.findAll();
        List<MemberCoupon> memberCouponList = allCoupons.stream()
                .map(coupon -> MemberCoupon.of(leader, coupon.getDiscountPrice(), coupon.getSerialNumber()))
                .collect(toList());
        memberCouponRepository.saveAll(memberCouponList);
    }

    public void createCoupon(Member member) {
        Coupon coupon = couponRepository.save(Coupon.builder()
                .serialNumber(UUID.randomUUID().toString())
                .discountPrice(1500)
                .title("서비스 런칭 기념 감사 쿠폰")
                .remainCount(10)
                .chargeCount(10)
                .available(true)
                .issueDate(now())
                .closeDate(now().plusDays(30))
                .receivePeople(0)
                .build());

        MemberCoupon memberCoupon = MemberCoupon.of(member, coupon.getDiscountPrice(), coupon.getSerialNumber());
        member.addCoupon(memberCoupon);
    }
}
