package com.gjgs.gjgs.modules.matching.aop;


import com.gjgs.gjgs.modules.exception.matching.AlreadyMatchingMemberException;
import com.gjgs.gjgs.modules.exception.member.MemberNotFoundException;
import com.gjgs.gjgs.modules.matching.repository.interfaces.MatchingQueryRepository;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class MatchingAspect {

    private final SecurityUtil securityUtil;
    private final MatchingQueryRepository matchingQueryRepository;

    @Before(value = "@annotation(CheckIsAlreadyMatching)")
    public void checkIsAlreadyMatching(JoinPoint joinPoint) {
        if (matchingQueryRepository.existsByUsername(getUsernameOrThrow())) {
            throw new AlreadyMatchingMemberException();
        }
    }

    private String getUsernameOrThrow() {
        return securityUtil.getCurrentUsername()
                .orElseThrow(() -> new MemberNotFoundException());
    }
}
