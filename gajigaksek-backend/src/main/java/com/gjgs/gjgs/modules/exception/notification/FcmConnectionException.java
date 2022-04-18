package com.gjgs.gjgs.modules.exception.notification;

import org.springframework.http.HttpStatus;

public class FcmConnectionException extends NotificationException{
    private static final String MESSAGE = "FCM 서버 연결에 실패했습니다.";
    private static final String CODE = "NOTIFICATION-405";

    public FcmConnectionException() {
        super(CODE, HttpStatus.INTERNAL_SERVER_ERROR, MESSAGE);
    }
}
