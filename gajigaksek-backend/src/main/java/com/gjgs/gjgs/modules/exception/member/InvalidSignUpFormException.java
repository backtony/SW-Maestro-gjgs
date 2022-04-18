package com.gjgs.gjgs.modules.exception.member;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

public class InvalidSignUpFormException extends MemberException{

    private static final String MESSAGE = "형식이 맞지 않습니다.";
    private static final String CODE = "FORM-400";

    public InvalidSignUpFormException(BindingResult errors) {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE, errors);
    }
}
