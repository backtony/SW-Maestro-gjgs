package com.gjgs.gjgs.modules.lecture.services.admin;

import com.gjgs.gjgs.modules.lecture.dtos.admin.DecideLectureType;
import com.gjgs.gjgs.modules.lecture.dtos.admin.RejectReason;

public interface DecideLecture {

    DecideLectureType getType();

    void decide(Long lectureId, RejectReason rejectReason);
}
