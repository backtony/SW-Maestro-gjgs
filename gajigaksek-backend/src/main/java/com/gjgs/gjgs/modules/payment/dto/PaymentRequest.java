package com.gjgs.gjgs.modules.payment.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotNull;

import static lombok.AccessLevel.PRIVATE;

@Getter @Setter @NoArgsConstructor(access = PRIVATE) @AllArgsConstructor(access = PRIVATE)  @Builder
public class PaymentRequest {

    @NotNull(message = "신청한 클래스 ID를 입력해주세요.")
    private Long lectureId;

    private Long memberCouponId;
    private int rewardAmount;
    private int totalPrice;

    @JsonIgnore
    @Builder.Default
    private int couponDiscountPrice = 0;

    @JsonIgnore
    public int getOriginalPrice() {
        return totalPrice + rewardAmount + couponDiscountPrice;
    }

    @JsonIgnore
    public int getTotalDiscountPrice() {
        return couponDiscountPrice + rewardAmount;
    }
}
