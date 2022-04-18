package com.gjgs.gjgs.modules.lecture.services.apply.timepolicy;

import java.time.LocalDateTime;

public interface EnableApplyScheduleTimePolicy {

    CheckTimeType getCheckTimeType();

    void checkCloseTime(LocalDateTime scheduleCloseTime);
}
