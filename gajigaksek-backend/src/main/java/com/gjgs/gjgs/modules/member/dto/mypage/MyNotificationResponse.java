package com.gjgs.gjgs.modules.member.dto.mypage;


import com.gjgs.gjgs.modules.notification.dto.NotificationDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@Builder
@Getter
public class MyNotificationResponse {

   @Builder.Default
   List<NotificationDto> notificationList = new ArrayList<>();

   public static MyNotificationResponse from(List<NotificationDto> notificationList){
      return MyNotificationResponse.builder()
              .notificationList(notificationList)
              .build();
   }
}
