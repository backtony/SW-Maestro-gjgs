package com.gjgs.gjgs.modules.lecture.services.admin;

import com.gjgs.gjgs.modules.exception.lecture.LectureNotFoundException;
import com.gjgs.gjgs.modules.lecture.dtos.admin.DecideLectureType;
import com.gjgs.gjgs.modules.lecture.dtos.admin.RejectReason;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RejectLecture implements DecideLecture {

    private final LectureRepository lectureRepository;

    @Override
    public DecideLectureType getType() {
        return DecideLectureType.REJECT;
    }

    @Override
    public void decide(Long lectureId, RejectReason rejectReason) {
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow(() -> new LectureNotFoundException());
        lecture.reject(rejectReason.getRejectReason());
    }
}
