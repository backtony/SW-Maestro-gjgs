package com.gjgs.gjgs.modules.lecture.aop;

import com.gjgs.gjgs.modules.exception.member.MemberNotFoundException;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.question.entity.Question;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class CheckDirectorAspect {

    private final SecurityUtil securityUtil;

    @AfterReturning(value = "@annotation(CheckDirector)", returning = "question")
    public void checkQuestionLectureDirector(Question question) {
        question.checkDirector(getTargetMember());
    }

    @AfterReturning(value = "@annotation(CheckNotDirector)", returning = "lecture")
    public void checkNotDirector(Lecture lecture) {
        lecture.checkNotDirector(lecture.getDirector(), getTargetMember());
    }

    private Member getTargetMember() {
        return Member.from(securityUtil.getCurrentUsername()
                .orElseThrow(() -> new MemberNotFoundException()));
    }
}
