package com.gjgs.gjgs.modules.question.dto;

import com.gjgs.gjgs.modules.member.dto.mypage.QuestionMainTextModifyRequest;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class QuestionRequest {

    @NotNull(message = "문의할 클래스의 ID가 필요합니다.")
    private Long lectureId;

    @NotNull(message = "문의할 내용을 넣어주세요.")
    private QuestionMainTextModifyRequest questionForm;
}
