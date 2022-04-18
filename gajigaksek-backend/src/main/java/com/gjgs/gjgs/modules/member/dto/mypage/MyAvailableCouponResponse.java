package com.gjgs.gjgs.modules.member.dto.mypage;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

@Getter @Setter @NoArgsConstructor(access = PRIVATE) @AllArgsConstructor(access = PRIVATE) @Builder
public class MyAvailableCouponResponse {

    @Builder.Default
    private Set<MyCoupon> myCouponList = new LinkedHashSet<>();

    public static MyAvailableCouponResponse of(Set<MyCoupon> myCouponList) {
        return MyAvailableCouponResponse.builder()
                .myCouponList(myCouponList)
                .build();
    }

    @Getter @Setter @NoArgsConstructor(access = PRIVATE) @Builder
    @EqualsAndHashCode(of = "memberCouponId")
    public static class MyCoupon {
        private Long memberCouponId;
        private String title;
        private int discountPrice;
        private LocalDateTime issueDate;
        private LocalDateTime closeDate;

        @QueryProjection
        public MyCoupon(Long memberCouponId, String title, int discountPrice, LocalDateTime issueDate, LocalDateTime closeDate) {
            this.memberCouponId = memberCouponId;
            this.title = title;
            this.discountPrice = discountPrice;
            this.issueDate = issueDate;
            this.closeDate = closeDate;
        }
    }
}
