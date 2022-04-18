package com.gjgs.gjgs.modules.exception.zone;

import org.springframework.http.HttpStatus;

public class ZoneNotFoundException extends ZoneException{

    private static final String MESSAGE = "등록되지 않은 지역입니다.";
    private static final String CODE = "ZONE-400";

    public ZoneNotFoundException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
