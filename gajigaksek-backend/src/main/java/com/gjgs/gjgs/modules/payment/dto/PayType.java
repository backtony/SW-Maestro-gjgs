package com.gjgs.gjgs.modules.payment.dto;

import com.gjgs.gjgs.modules.utils.document.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum PayType implements EnumType {

    PERSONAL("개인 결제"),
    TEAM("팀 결제")
    ;

    private final String description;

    @Override
    public String getName() {
        return this.name();
    }
}
