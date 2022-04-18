package com.gjgs.gjgs.modules.exception.lecture;

import org.springframework.http.HttpStatus;

public class InvalidDecideLectureTypeException extends LectureException{

    private static final String MESSAGE = "유효한 클래스 검수 결정이 아닙니다.";
    private static final String CODE = "LECTURE-400";

    public InvalidDecideLectureTypeException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
