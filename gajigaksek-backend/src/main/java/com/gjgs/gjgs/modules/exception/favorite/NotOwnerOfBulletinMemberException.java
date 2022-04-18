package com.gjgs.gjgs.modules.exception.favorite;

import org.springframework.http.HttpStatus;

public class NotOwnerOfBulletinMemberException extends FavoriteException{

    private static final String MESSAGE = "해당 찜의 소유자가 아닙니다.";
    private static final String CODE = "BULLETIN-MEMBER-403";

    public NotOwnerOfBulletinMemberException() {
        super(CODE, HttpStatus.FORBIDDEN, MESSAGE);
    }

}
