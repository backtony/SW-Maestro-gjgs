package com.gjgs.gjgs.modules.coupon.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Getter @Setter @NoArgsConstructor(access = PRIVATE) @AllArgsConstructor(access = PRIVATE) @Builder
public class DirectorCouponResponse {

    private List<CouponResponse> couponResponseList;

    public static DirectorCouponResponse of(List<CouponResponse> couponResponseList) {
        return DirectorCouponResponse.builder()
                .couponResponseList(couponResponseList)
                .build();
    }

    @Getter @Setter @NoArgsConstructor(access = PRIVATE) @Builder
    public static class CouponResponse {

        private Long lectureId;
        private String title;
        private LocalDateTime issueDate;
        private LocalDateTime closeDate;
        private int discountPrice;
        private int chargeCount;
        private int receivePeople;
        private int remainCount;

        @QueryProjection
        public CouponResponse(Long lectureId, String title, LocalDateTime issueDate, LocalDateTime closeDate, int discountPrice, int chargeCount, int receivePeople, int remainCount) {
            this.lectureId = lectureId;
            this.title = title;
            this.issueDate = issueDate;
            this.closeDate = closeDate;
            this.discountPrice = discountPrice;
            this.chargeCount = chargeCount;
            this.receivePeople = receivePeople;
            this.remainCount = remainCount;
        }
    }
}
