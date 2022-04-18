package com.gjgs.gjgs.modules.lecture.entity;

import com.gjgs.gjgs.modules.exception.lecture.InvalidActionCauseIAmDirectorException;
import com.gjgs.gjgs.modules.member.entity.Member;

public interface CheckLectureDirector {

    default void checkNotDirector(Member director, Member target) {
        if (isDirector(director, target)) {
            throw new InvalidActionCauseIAmDirectorException();
        }
    }

    private boolean isDirector(Member director, Member target) {
        if (target.getId() != null) {
            return director.getId().equals(target.getId());
        }
        return director.getUsername().equals(target.getUsername());
    }
}
