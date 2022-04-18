package com.gjgs.gjgs.modules.exception.lecture;

import org.springframework.http.HttpStatus;

public class InvalidActionCauseIAmDirectorException extends LectureException{

    private static final String MESSAGE = "해당 기능은 클래스 개설자가 본인일 경우 이용할 수 없습니다.";
    private static final String CODE = "LECTURE-400";

    public InvalidActionCauseIAmDirectorException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
