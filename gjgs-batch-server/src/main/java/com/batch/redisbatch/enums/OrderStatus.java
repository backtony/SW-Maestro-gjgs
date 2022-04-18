package com.batch.redisbatch.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static lombok.AccessLevel.PROTECTED;

@Getter
@AllArgsConstructor(access = PROTECTED)
public enum OrderStatus {

    WAIT("결제 대기 중"),
    COMPLETE("결제 완료"),
    CANCEL("결제 취소")
    ;

    private String description;
}
