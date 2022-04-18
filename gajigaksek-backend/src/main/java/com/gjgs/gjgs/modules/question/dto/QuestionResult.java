package com.gjgs.gjgs.modules.question.dto;

import com.gjgs.gjgs.modules.utils.document.EnumType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public enum QuestionResult implements EnumType {

    QUESTION("질문글을 작성했습니다."),
    REPLY("질문글에 대해 답글을 남겼습니다.");

    private String description;

    QuestionResult(String description) {
        this.description = description;
    }

    @Override
    public String getName() {
        return this.name();
    }
}
