package com.gjgs.gjgs.modules.exception.lecture;

import org.springframework.http.HttpStatus;

public class ExistCreatingLectureException extends LectureException{

    private static final String MESSAGE = "이미 작성중인 클래스가 있습니다. 작성중인 클래스 혹은 검수 거절된 클래스를 삭제해주세요.";
    private static final String CODE = "LECTURE-409";

    public ExistCreatingLectureException() {
        super(CODE, HttpStatus.CONFLICT, MESSAGE);
    }
}
