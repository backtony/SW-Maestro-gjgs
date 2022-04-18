package com.gjgs.gjgs.modules.exception.lecture;

import org.springframework.http.HttpStatus;

public class NotRejectLectureException extends LectureException{
    private static final String MESSAGE = "검수 거절된 클래스가 아닙니다.";
    private static final String CODE = "LECTURE-400";

    public NotRejectLectureException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
