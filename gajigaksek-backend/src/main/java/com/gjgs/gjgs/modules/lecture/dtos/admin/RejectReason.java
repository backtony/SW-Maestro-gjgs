package com.gjgs.gjgs.modules.lecture.dtos.admin;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

import static lombok.AccessLevel.PRIVATE;

@Getter @Setter @NoArgsConstructor(access = PRIVATE) @AllArgsConstructor(access = PRIVATE) @Builder
public class RejectReason {

    @NotBlank(message = "검수 거절 사유를 입력해주세요.")
    @Length(min = 10, message = "검수 거절 사유를 10자 이상 입력해주세요.")
    private String rejectReason;
}
