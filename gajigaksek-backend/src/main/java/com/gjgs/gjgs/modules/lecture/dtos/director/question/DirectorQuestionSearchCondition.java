package com.gjgs.gjgs.modules.lecture.dtos.director.question;


import com.gjgs.gjgs.modules.question.enums.QuestionStatus;
import lombok.*;

import javax.validation.constraints.NotNull;

import static lombok.AccessLevel.PRIVATE;
@Getter @Setter @NoArgsConstructor(access = PRIVATE) @AllArgsConstructor(access = PRIVATE) @Builder
public class DirectorQuestionSearchCondition {

    @NotNull(message = "WAIT(답변 대기중), COMPLETE(답변 완료) 둘 중 하나를 입력해주세요.")
    private QuestionStatus questionStatus;

    private Long lectureId;
}
