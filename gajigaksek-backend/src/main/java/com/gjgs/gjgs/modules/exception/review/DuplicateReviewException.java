package com.gjgs.gjgs.modules.exception.review;

import org.springframework.http.HttpStatus;

public class DuplicateReviewException extends ReviewException{

    private static final String MESSAGE = "회원은 이미 해당 클래스에 리뷰를 작성하였습니다.";
    private static final String CODE = "REVIEW-409";

    public DuplicateReviewException() {
        super(CODE, HttpStatus.CONFLICT, MESSAGE);
    }
}
