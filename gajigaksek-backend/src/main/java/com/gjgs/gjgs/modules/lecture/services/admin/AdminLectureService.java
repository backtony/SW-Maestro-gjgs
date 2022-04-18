package com.gjgs.gjgs.modules.lecture.services.admin;

import com.gjgs.gjgs.modules.lecture.dtos.admin.ConfirmLectureResponse;
import com.gjgs.gjgs.modules.lecture.dtos.admin.DecideLectureType;
import com.gjgs.gjgs.modules.lecture.dtos.admin.RejectReason;
import com.gjgs.gjgs.modules.lecture.dtos.create.PutLectureResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminLectureService {

    Page<ConfirmLectureResponse> getConfirmLectures(Pageable pageable);

    PutLectureResponse getConfirmLecture(Long lectureId);

    void decideLecture(Long lectureId, DecideLectureType decideType, RejectReason rejectReason);
}
