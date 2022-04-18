package com.gjgs.gjgs.modules.lecture.repositories.lecture;

import com.gjgs.gjgs.modules.lecture.dtos.LectureDetailResponse;
import com.gjgs.gjgs.modules.lecture.dtos.create.PutLectureResponse;
import com.gjgs.gjgs.modules.lecture.dtos.director.lecture.DirectorLectureResponse;
import com.gjgs.gjgs.modules.lecture.dtos.director.lecture.GetLectureType;
import com.gjgs.gjgs.modules.lecture.dtos.director.schedule.DirectorScheduleResponse;
import com.gjgs.gjgs.modules.lecture.dtos.search.LectureSearchResponse;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface LectureQueryRepository {

    Optional<Lecture> findWithCategoryZoneByDirectorUsername(String username);

    Optional<LectureDetailResponse> findLectureDetail(Long lectureId);

    Optional<DirectorLectureResponse> findDirectorLectures(String username, GetLectureType type);

    Optional<Lecture> findWithFinishedProductsByDirectorUsername(String username);

    Optional<Lecture> findWithCurriculumsByDirectorUsername(String username);

    Optional<Lecture> findWithSchedulesByDirectorUsername(String username);

    Optional<Lecture> findAcceptLectureWithSchedulesByIdUsername(Long lectureId, String username);

    Optional<DirectorScheduleResponse> findSchedulesByLectureIdUsername(Long lectureId, String username);

    Optional<Lecture> findWithScheduleByLectureScheduleId(Long lectureId, Long scheduleId);

    Optional<Lecture> findByIdDirectorUsername(Long lectureId, String username);

    Optional<PutLectureResponse> findRejectLectureById(Long lectureId);

    boolean existCreatingLectureByUsername(String username);

    Optional<Lecture> findRejectLectureEntityById(Long lectureId);

    long deleteFinishedProductsByLectureId(Long lectureId);

    long deleteCurriculumsByLectureId(Long lectureId);

    long deleteSchedulesByLectureId(Long lectureId);

    Slice<LectureSearchResponse> findDirectorLecturesByDirectorId(Pageable pageable, Long directorId);

    Optional<Lecture> findWithDirectorById(Long lectureId);

    long updateLectureScore(Long lectureId, Double scoreAvg);

    void updateViewCount(Long lectureId);
}
