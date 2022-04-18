package com.gjgs.gjgs.modules.lecture.repositories.temporaryStore;

import com.gjgs.gjgs.modules.lecture.dtos.admin.ConfirmLectureResponse;
import com.gjgs.gjgs.modules.lecture.dtos.create.PutLectureResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TemporaryStoredLectureQueryRepository {

    Optional<PutLectureResponse> findByDirectorUsername(String username);

    Page<ConfirmLectureResponse> findConfirmLectures(Pageable pageable);

    Optional<PutLectureResponse> findConfirmLectureById(Long lectureId);
}
