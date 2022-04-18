package com.gjgs.gjgs.modules.lecture.services.admin;

import com.gjgs.gjgs.modules.exception.lecture.LectureNotFoundException;
import com.gjgs.gjgs.modules.lecture.dtos.admin.ConfirmLectureResponse;
import com.gjgs.gjgs.modules.lecture.dtos.admin.DecideLectureType;
import com.gjgs.gjgs.modules.lecture.dtos.admin.RejectReason;
import com.gjgs.gjgs.modules.lecture.dtos.create.PutLectureResponse;
import com.gjgs.gjgs.modules.lecture.repositories.temporaryStore.TemporaryStoredLectureQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminLectureServiceImpl implements AdminLectureService {

    private final TemporaryStoredLectureQueryRepository tempLectureRepository;
    private final LectureDecideStrategyFactory strategyFactory;

    @Override
    @Transactional(readOnly = true)
    public Page<ConfirmLectureResponse> getConfirmLectures(Pageable pageable) {
        return tempLectureRepository.findConfirmLectures(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public PutLectureResponse getConfirmLecture(Long lectureId) {
        return tempLectureRepository.findConfirmLectureById(lectureId).orElseThrow(() -> new LectureNotFoundException());
    }

    @Override
    public void decideLecture(Long lectureId, DecideLectureType decideType, RejectReason rejectReason) {
        strategyFactory.getStrategy(decideType).decide(lectureId, rejectReason);
    }
}
