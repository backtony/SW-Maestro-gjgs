package com.gjgs.gjgs.modules.notice.aop;

import com.gjgs.gjgs.modules.exception.member.InvalidAuthorityException;
import com.gjgs.gjgs.modules.exception.notice.NoticeTypeException;
import com.gjgs.gjgs.modules.member.enums.Authority;
import com.gjgs.gjgs.modules.notice.enums.NoticeType;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class NoticeAspect {

    private final SecurityUtil securityUtil;
    private static final String ALL = "ALL";
    private static final String DIRECTOR = "DIRECTOR";

    @Before("@annotation(CheckAuthority) && args(noticeType,..)")
    public void checkAuthority(JoinPoint joinPoint, String noticeType) {
        if (!noticeType.equals(ALL) && !noticeType.equals(DIRECTOR)){
            throw new NoticeTypeException();
        }
        if(NoticeType.valueOf(noticeType).equals(NoticeType.DIRECTOR) &&
                    getAuthorityOrThrow().equals(Authority.ROLE_USER)){
            throw new InvalidAuthorityException();
        }
    }

    private Authority getAuthorityOrThrow(){
        return securityUtil.getAuthority()
                .orElseThrow(() -> new InvalidAuthorityException());
    }
}
