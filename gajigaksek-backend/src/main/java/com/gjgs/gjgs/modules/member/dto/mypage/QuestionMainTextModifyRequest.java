package com.gjgs.gjgs.modules.member.dto.mypage;

import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionMainTextModifyRequest {

    @Length(min = 10, max = 1000, message = "문의글은 10자 이상 1000자 이하입니다.")
    private String mainText;

    public static QuestionMainTextModifyRequest from(String mainText) {
        return QuestionMainTextModifyRequest.builder()
                .mainText(mainText)
                .build();
    }
}
