package com.gjgs.gjgs.modules.payment.enums;

import com.gjgs.gjgs.modules.utils.document.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static lombok.AccessLevel.PROTECTED;

@Getter
@AllArgsConstructor(access = PROTECTED)
public enum OrderStatus implements EnumType {

    WAIT("결제 대기 중"),
    COMPLETE("결제 완료"),
    CANCEL("결제 취소")
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
