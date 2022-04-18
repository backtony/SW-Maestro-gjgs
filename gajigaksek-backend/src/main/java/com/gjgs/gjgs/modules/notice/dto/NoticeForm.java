package com.gjgs.gjgs.modules.notice.dto;

import com.gjgs.gjgs.modules.notice.enums.NoticeType;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeForm {

    @NotBlank
    private String title;

    @NotBlank
    private String text;

    @NotNull
    private NoticeType noticeType;
}
