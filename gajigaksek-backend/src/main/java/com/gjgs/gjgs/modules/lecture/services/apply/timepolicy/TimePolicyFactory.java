package com.gjgs.gjgs.modules.lecture.services.apply.timepolicy;

import com.gjgs.gjgs.modules.exception.lecture.InvalidApplyTypeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
public class TimePolicyFactory {

    private final List<EnableApplyScheduleTimePolicy> policyList;

    public EnableApplyScheduleTimePolicy getPolicy(CheckTimeType timeType) {
        return policyList.stream()
                .filter(policy -> policy.getCheckTimeType().equals(timeType))
                .findFirst()
                .orElseThrow(() -> new InvalidApplyTypeException());
    }
}
