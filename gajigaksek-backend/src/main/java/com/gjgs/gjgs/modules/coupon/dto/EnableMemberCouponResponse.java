package com.gjgs.gjgs.modules.coupon.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

@Getter @Setter @NoArgsConstructor(access = PRIVATE) @AllArgsConstructor(access = PRIVATE) @Builder
public class EnableMemberCouponResponse {

    @Builder.Default
    private Set<EnableCoupon> enableCouponList = new LinkedHashSet<>();

    public static EnableMemberCouponResponse of(Set<EnableCoupon> enableCouponSet) {
        return EnableMemberCouponResponse.builder()
                .enableCouponList(enableCouponSet)
                .build();
    }

    @Getter @Setter @NoArgsConstructor(access = PRIVATE) @Builder @EqualsAndHashCode(of = "memberCouponId")
    public static class EnableCoupon {
        private Long memberCouponId;
        private String couponTitle;
        private int discountPrice;

        @QueryProjection
        public EnableCoupon(Long memberCouponId, String couponTitle, int discountPrice) {
            this.memberCouponId = memberCouponId;
            this.couponTitle = couponTitle;
            this.discountPrice = discountPrice;
        }
    }
}
