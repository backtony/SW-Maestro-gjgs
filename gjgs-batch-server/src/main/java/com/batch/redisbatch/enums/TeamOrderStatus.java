package com.batch.redisbatch.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static lombok.AccessLevel.PROTECTED;

@Getter
@AllArgsConstructor(access = PROTECTED)
public enum TeamOrderStatus {

    WAIT("팀원들의 결제 대기"),
    COMPLETE("팀원 모두 결제 완료"),
    CANCEL("30분 이후 취소된 결제")
    ;

    private String description;
}
