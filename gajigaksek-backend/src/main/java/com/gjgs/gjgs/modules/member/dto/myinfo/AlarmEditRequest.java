package com.gjgs.gjgs.modules.member.dto.myinfo;


import com.gjgs.gjgs.modules.member.enums.Alarm;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlarmEditRequest {

    private Alarm type;

    private boolean isActive;
}
