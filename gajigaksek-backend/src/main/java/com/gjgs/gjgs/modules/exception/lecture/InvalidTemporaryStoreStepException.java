package com.gjgs.gjgs.modules.exception.lecture;

import org.springframework.http.HttpStatus;

public class InvalidTemporaryStoreStepException extends LectureException{

    private static final String MESSAGE = "존재하지 않는 임시 저장된 클래스입니다.";
    private static final String CODE = "LECTURE-404";

    public InvalidTemporaryStoreStepException() {
        super(CODE, HttpStatus.NOT_FOUND, MESSAGE);
    }
}
