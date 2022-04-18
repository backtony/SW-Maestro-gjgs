package com.gjgs.gjgs.modules.notification.service.interfaces;

import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.notification.dto.NotificationCreateRequest;
import com.gjgs.gjgs.modules.notification.enums.NotificationType;
import com.gjgs.gjgs.modules.team.entity.Team;

import java.util.List;

public interface NotificationService {

    void sendCustomNotification(NotificationCreateRequest notificationCreateRequest);

    void readNotification(String uuid);

    void sendMatchingNotification(List<Member> memberList, Long teamId, NotificationType notificationType);

    void sendApplyNotification(Team team);

}
