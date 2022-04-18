package com.gjgs.gjgs.modules.coupon.entity;

import com.gjgs.gjgs.modules.exception.coupon.AvailableCouponException;
import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLecture;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.utils.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor(access = AccessLevel.PROTECTED) @Builder
@EqualsAndHashCode(of = "id", callSuper = false)
public class Coupon extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY) @Column(name = "COUPON_ID")
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "LECTURE_ID")
    private Lecture lecture;

    @Column(unique = true)
    private String serialNumber;

    @Column(nullable = false)
    private int discountPrice;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int remainCount;

    @Column(nullable = false)
    private int chargeCount;

    @Column(nullable = false)
    private boolean available;

    private LocalDateTime issueDate;

    private LocalDateTime closeDate;

    @Column(nullable = false)
    private int receivePeople;

    @Transient
    private static final String TITLE_TEMPLATE = " 원 할인 쿠폰";

    public static Coupon of(Lecture lecture, CreateLecture.CouponDto coupon) {
        return build(lecture, coupon);
    }

    private static Coupon build(Lecture lecture, CreateLecture.CouponDto couponRequest) {
        StringBuilder builder = new StringBuilder();
        DecimalFormat decimalFormat = new DecimalFormat("###,###");

        return Coupon.builder()
                .lecture(lecture)
                .discountPrice(couponRequest.getCouponPrice())
                .chargeCount(couponRequest.getCouponCount())
                .remainCount(couponRequest.getCouponCount())
                .receivePeople(0)
                .title(builder.append(lecture.getTitle())
                        .append(" ").append(decimalFormat.format(couponRequest.getCouponPrice()))
                        .append(TITLE_TEMPLATE)
                        .toString())
                .available(false)
                .build();
    }

    public void update(CreateLecture.CouponDto couponRequest) {
        this.discountPrice = couponRequest.getCouponPrice();
        this.chargeCount = couponRequest.getCouponCount();
        this.remainCount = couponRequest.getCouponCount();
    }

    public void minusCount() {
        this.remainCount--;
        this.receivePeople++;
        checkCouponStatus();
    }

    private void checkCouponStatus() {
        if (this.getRemainCount() == 0) {
            this.available = false;
        }
    }

    public void issueCoupon(CreateLecture.CouponDto couponDto) {
        isAvailableDoThrow();
        this.discountPrice = couponDto.getCouponPrice();
        this.chargeCount = couponDto.getCouponCount();
        this.remainCount = chargeCount;
        this.available = true;
        this.issueDate = now();
        this.closeDate = issueDate.plusDays(30);
        this.receivePeople = 0;
    }

    private void isAvailableDoThrow() {
        if (isAvailable()) {
            throw new AvailableCouponException();
        }
    }

    public void closeCoupon() {
        this.remainCount = 0;
        this.available = false;
    }

    public void activateWithLecture() {
        this.serialNumber = UUID.randomUUID().toString();
        this.available = true;
        this.issueDate = now();
        this.closeDate = issueDate.plusDays(30);

        this.lecture.accept();
    }
}
