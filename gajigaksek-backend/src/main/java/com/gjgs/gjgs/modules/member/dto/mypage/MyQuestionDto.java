package com.gjgs.gjgs.modules.member.dto.mypage;


import com.gjgs.gjgs.modules.question.enums.QuestionStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class MyQuestionDto {

    private Long questionId;

    private String classTitle;

    private String directorNickname;

    private String mainText;

    private String replyText;

    private QuestionStatus questionStatus;

    private LocalDateTime questionDate;

    @QueryProjection
    public MyQuestionDto(Long questionId, String classTitle, String directorNickname, String mainText, String replyText, QuestionStatus questionStatus, LocalDateTime questionDate) {
        this.questionId = questionId;
        this.classTitle = classTitle;
        this.directorNickname = directorNickname;
        this.mainText = mainText;
        this.replyText = replyText;
        this.questionStatus = questionStatus;
        this.questionDate = questionDate;
    }
}
