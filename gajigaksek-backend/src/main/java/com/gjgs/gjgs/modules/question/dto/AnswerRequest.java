package com.gjgs.gjgs.modules.question.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AnswerRequest {

    @Length(min = 10, max = 1000, message = "답글은 10자 이상 1000자 이하로 작성해주세요.")
    private String replyText;
}
