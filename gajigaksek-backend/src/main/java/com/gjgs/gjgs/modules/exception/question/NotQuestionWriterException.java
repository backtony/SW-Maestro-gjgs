package com.gjgs.gjgs.modules.exception.question;

import org.springframework.http.HttpStatus;

public class NotQuestionWriterException extends QuestionException{

    private static final String MESSAGE = "해당 문의의 작성자가 아닙니다.";
    private static final String CODE = "QUESTION-403";

    public NotQuestionWriterException() {
        super(CODE, HttpStatus.FORBIDDEN, MESSAGE);
    }
}
