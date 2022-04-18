package com.gjgs.gjgs.modules.question.enums;

import com.gjgs.gjgs.modules.utils.document.EnumType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public enum QuestionStatus implements EnumType {

    WAIT("답변 대기중"),
    COMPLETE("답변 완료");

    private String description;

    QuestionStatus(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getName() {
        return this.name();
    }
}
