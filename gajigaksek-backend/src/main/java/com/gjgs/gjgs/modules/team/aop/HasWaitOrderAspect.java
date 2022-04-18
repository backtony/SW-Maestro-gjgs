package com.gjgs.gjgs.modules.team.aop;

import com.gjgs.gjgs.modules.exception.payment.TeamHaveWaitOrderException;
import com.gjgs.gjgs.modules.payment.repository.OrderQueryRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class HasWaitOrderAspect {

    private final OrderQueryRepository orderQueryRepository;

    @Before("@annotation(HasWaitOrder)")
    public void checkHasWaitOrder(JoinPoint joinPoint) {
        Long teamId = (Long)joinPoint.getArgs()[0];
        if (orderQueryRepository.existWaitOrderTeamByTeamId(teamId)) {
            throw new TeamHaveWaitOrderException();
        }
    }
}
