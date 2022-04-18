package com.gjgs.gjgs.modules.notification.dto;

import com.gjgs.gjgs.modules.notification.enums.TargetType;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationCreateRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String message;

    @NotNull
    private TargetType targetType;

    @Builder.Default
    private List<Long> memberIdList = new ArrayList<>();

    private Long teamId;




}
