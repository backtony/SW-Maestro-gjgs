package com.gjgs.gjgs.modules.exception.lecture;

import org.springframework.http.HttpStatus;

public class NotExistConditionException extends LectureException{

    private static final String MESSAGE = "내가 진행하는 클래스를 찾는 조건이 맞지 않습니다.";
    private static final String CODE = "LECTURE-400";

    public NotExistConditionException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
