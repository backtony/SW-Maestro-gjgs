package com.gjgs.gjgs.modules.exception.question;

import org.springframework.http.HttpStatus;

public class CanNotQuestionToMyClassException extends QuestionException{

    private static final String MESSAGE = "해당 클래스의 디렉터는 문의글을 작성할 수 없습니다.";
    private static final String CODE = "QUESTION-409";

    public CanNotQuestionToMyClassException() {
        super(CODE, HttpStatus.CONFLICT, MESSAGE);
    }
}
