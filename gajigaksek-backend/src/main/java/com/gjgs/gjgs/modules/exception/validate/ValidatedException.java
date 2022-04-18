package com.gjgs.gjgs.modules.exception.validate;

import com.gjgs.gjgs.modules.exception.ApplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

public class ValidatedException extends ApplicationException {
    private static final String MESSAGE = "조건에 맞지 않는 입력값 입니다.";
    private static final String CODE = "FORM-400";

    public ValidatedException(BindingResult errors) {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE, errors);
    }
}
