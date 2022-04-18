package com.gjgs.gjgs.modules.exception.notification;

import org.springframework.http.HttpStatus;

public class NotificationTypeException extends NotificationException{

    private static final String MESSAGE = "존재하지 않는 대상 타입입니다.";
    private static final String CODE = "NOTIFICATION-400";

    public NotificationTypeException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
