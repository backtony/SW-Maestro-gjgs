package com.gjgs.gjgs.modules.exception.question;

import org.springframework.http.HttpStatus;

public class QuestionNotFoundException extends QuestionException{

    private static final String MESSAGE = "존재하지 않는 문의입니다.";
    private static final String CODE = "QUESTION-404";

    public QuestionNotFoundException() {
        super(CODE, HttpStatus.NOT_FOUND, MESSAGE);
    }
}
