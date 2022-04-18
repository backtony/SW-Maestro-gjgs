package com.batch.redisbatch.service.policy;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor(access = PRIVATE)
public enum DiscountType {

    NOTHING("할인 적용 되지 않음"),
    COUPON("쿠폰 적용"),
    REWARD("리워드 적용"),
    COUPON_REWARD("쿠폰 리워드 모두 적용")
    ;

    private String description;
}
