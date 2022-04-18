package com.gjgs.gjgs.modules.exception.review;

import org.springframework.http.HttpStatus;

public class EmptyReviewException extends ReviewException{

    private static final String MESSAGE = "리뷰가 작성되지 않았습니다.";
    private static final String CODE = "REVIEW-409";

    protected EmptyReviewException(String errorCode, HttpStatus httpStatus, String message) {
        super(CODE, HttpStatus.CONFLICT, MESSAGE);
    }
}
