package com.gjgs.gjgs.modules.exception.review;

import org.springframework.http.HttpStatus;

public class NotExistReplyReviewException extends ReviewException{

    private static final String MESSAGE = "답글을 작성할 리뷰가 없습니다.";
    private static final String CODE = "REVIEW-404";

    public NotExistReplyReviewException() {
        super(CODE, HttpStatus.NOT_FOUND, MESSAGE);
    }
}
