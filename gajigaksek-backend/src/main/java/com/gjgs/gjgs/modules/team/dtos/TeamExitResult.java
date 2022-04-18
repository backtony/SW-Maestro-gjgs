package com.gjgs.gjgs.modules.team.dtos;

import com.gjgs.gjgs.modules.utils.document.EnumType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public enum TeamExitResult implements EnumType {

    EXIT("팀을 나갔습니다."),
    EXCLUDE("팀원을 추방했습니다."),
    DELETE("팀을 삭제했습니다.");

    private String description;

    TeamExitResult(String description) {
        this.description = description;
    }

    @Override
    public String getName() {
        return this.name();
    }
}
