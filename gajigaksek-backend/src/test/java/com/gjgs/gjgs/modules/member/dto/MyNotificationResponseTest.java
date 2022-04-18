package com.gjgs.gjgs.modules.member.dto;


import com.gjgs.gjgs.modules.dummy.NotificationDummy;
import com.gjgs.gjgs.modules.member.dto.mypage.MyNotificationResponse;
import com.gjgs.gjgs.modules.notification.dto.NotificationDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MyNotificationResponseTest {

    @Test
    @DisplayName("from 생성 테스트")
    void from() throws Exception{
        //given
        List<NotificationDto> notificationDtoList = NotificationDummy.createNotificationDtoList();


        //when
        MyNotificationResponse from = MyNotificationResponse.from(notificationDtoList);

        //then
        assertThat(from.getNotificationList().size()).isEqualTo(notificationDtoList.size());
    }
}
