package com.gjgs.gjgs.modules.exception.bulletin;

import org.springframework.http.HttpStatus;

public class BulletinNotFoundOrNotLeaderException extends BulletinException{

    private static final String MESSAGE = "해당 모집 게시글이 존재하지 않거나, 그룹장이 아닙니다.";
    private static final String CODE = "BULLETIN-409";

    public BulletinNotFoundOrNotLeaderException() {
        super(CODE, HttpStatus.CONFLICT, MESSAGE);
    }
}
