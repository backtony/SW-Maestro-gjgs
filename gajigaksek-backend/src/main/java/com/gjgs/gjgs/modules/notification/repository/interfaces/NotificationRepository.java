package com.gjgs.gjgs.modules.notification.repository.interfaces;

import com.gjgs.gjgs.modules.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NotificationRepository extends JpaRepository<Notification,Long> {
}
