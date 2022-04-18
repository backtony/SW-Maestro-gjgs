package com.batch.redisbatch.repository.interfaces;

import com.batch.redisbatch.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NotificationRepository extends JpaRepository<Notification,Long> {
}
