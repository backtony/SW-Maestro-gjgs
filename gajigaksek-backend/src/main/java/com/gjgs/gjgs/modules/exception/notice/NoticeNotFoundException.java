package com.gjgs.gjgs.modules.exception.notice;

import org.springframework.http.HttpStatus;

public class NoticeNotFoundException extends NoticeException{

    private static final String MESSAGE = "해당 id를 가진 공지사항이 없습니다.";
    private static final String CODE = "NOTICE-400";

    public NoticeNotFoundException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
