package com.gjgs.gjgs.modules.lecture.services.apply;

import com.gjgs.gjgs.modules.lecture.dtos.apply.ApplyLectureTeamRequest;
import com.gjgs.gjgs.modules.lecture.dtos.apply.ApplyLectureTeamResponse;
import com.siot.IamportRestClient.exception.IamportResponseException;

import java.io.IOException;

public interface ApplyScheduleTeamService {

    ApplyLectureTeamResponse apply(Long scheduleId, ApplyLectureTeamRequest request);

    void cancel(Long scheduleId, Long teamId) throws IamportResponseException, IOException;
}
