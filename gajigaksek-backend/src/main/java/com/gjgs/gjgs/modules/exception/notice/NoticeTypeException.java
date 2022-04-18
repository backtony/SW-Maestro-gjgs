package com.gjgs.gjgs.modules.exception.notice;

import org.springframework.http.HttpStatus;

public class NoticeTypeException extends NoticeException {

    private static final String MESSAGE = "존재하지 않는 Notice Type 입니다.";
    private static final String CODE = "NOTICE-400";

    public NoticeTypeException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
