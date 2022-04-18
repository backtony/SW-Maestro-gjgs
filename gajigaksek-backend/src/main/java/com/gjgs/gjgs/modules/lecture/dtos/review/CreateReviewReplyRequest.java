package com.gjgs.gjgs.modules.lecture.dtos.review;


import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

import static lombok.AccessLevel.PRIVATE;

@Getter @Setter @NoArgsConstructor(access = PRIVATE) @AllArgsConstructor(access = PRIVATE) @Builder
public class CreateReviewReplyRequest {

    @NotBlank(message = "후기 답글을 작성해주세요.")
    @Length(min = 10, message = "답글은 최소 10자 이상 입력해주세요.")
    private String replyText;
}
