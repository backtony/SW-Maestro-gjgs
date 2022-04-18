package com.gjgs.gjgs.modules.notification.repository.interfaces;

import com.gjgs.gjgs.modules.notification.entity.Notification;

import java.util.List;

public interface NotificationJdbcRepository {

    void insertNotification(List<Notification> notificationList);



}
