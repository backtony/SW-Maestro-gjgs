package com.gjgs.gjgs.modules.exception.question;

import org.springframework.http.HttpStatus;

public class NotTheOwnerOfClassException extends QuestionException{

    private static final String MESSAGE = "답글은 해당 클래스의 디렉터만 작성 가능합니다.";
    private static final String CODE = "QUESTION-403";

    public NotTheOwnerOfClassException() {
        super(CODE, HttpStatus.FORBIDDEN, MESSAGE);
    }
}
