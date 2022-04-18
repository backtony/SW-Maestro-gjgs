package com.gjgs.gjgs.modules.payment.enums;

import com.gjgs.gjgs.modules.utils.document.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static lombok.AccessLevel.PROTECTED;

@Getter
@AllArgsConstructor(access = PROTECTED)
public enum TeamOrderStatus implements EnumType {

    WAIT("팀원들의 결제 대기"),
    COMPLETE("팀원 모두 결제 완료"),
    CANCEL("30분 이후 취소된 결제")
    ;

    private String description;

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getName() {
        return this.name();
    }
}
