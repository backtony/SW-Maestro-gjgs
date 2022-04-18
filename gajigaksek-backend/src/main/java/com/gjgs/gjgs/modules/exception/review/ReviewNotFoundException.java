package com.gjgs.gjgs.modules.exception.review;

import org.springframework.http.HttpStatus;

public class ReviewNotFoundException extends ReviewException{
    private static final String MESSAGE = "해당 리뷰는 존재하지 않습니다.";
    private static final String CODE = "REVIEW-404";

    public ReviewNotFoundException() {
        super(CODE, HttpStatus.NOT_FOUND, MESSAGE);
    }
}
