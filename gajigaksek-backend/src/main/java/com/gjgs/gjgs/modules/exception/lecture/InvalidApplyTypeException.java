package com.gjgs.gjgs.modules.exception.lecture;

import org.springframework.http.HttpStatus;

public class InvalidApplyTypeException extends LectureException{

    private static final String MESSAGE = "유효한 클래스 신청 방법이 아닙니다. 팀 신청과 개인 신청만 존재합니다.";
    private static final String CODE = "LECTURE-400";

    public InvalidApplyTypeException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
