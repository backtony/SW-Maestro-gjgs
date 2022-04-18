package com.gjgs.gjgs.modules.exception.matching;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

public class MatchingFormErrorException extends MatchingException {

    private static final String MESSAGE = "매칭 폼에 적절하지 못한 값이 입력되었습니다.";
    private static final String CODE = "MATCH-400";

    public MatchingFormErrorException(BindingResult errors) {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE, errors);
    }
}
