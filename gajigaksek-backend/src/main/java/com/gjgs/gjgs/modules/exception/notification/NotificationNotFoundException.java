package com.gjgs.gjgs.modules.exception.notification;

import org.springframework.http.HttpStatus;

public class NotificationNotFoundException extends NotificationException{

    private static final String MESSAGE = "존재하지 않는 알림입니다.";
    private static final String CODE = "NOTIFICATION-400";

    public NotificationNotFoundException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
