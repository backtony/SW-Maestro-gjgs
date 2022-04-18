package com.gjgs.gjgs.modules.lecture.services.director.lecture;

import com.gjgs.gjgs.modules.exception.lecture.DirectorHaveNotLectureException;
import com.gjgs.gjgs.modules.exception.lecture.ExistCreatingLectureException;
import com.gjgs.gjgs.modules.exception.lecture.NotRejectLectureException;
import com.gjgs.gjgs.modules.exception.member.MemberNotFoundException;
import com.gjgs.gjgs.modules.lecture.dtos.create.PutLectureResponse;
import com.gjgs.gjgs.modules.lecture.dtos.director.lecture.DirectorLectureResponse;
import com.gjgs.gjgs.modules.lecture.dtos.director.lecture.GetLectureType;
import com.gjgs.gjgs.modules.lecture.dtos.director.question.DirectorQuestionResponse;
import com.gjgs.gjgs.modules.lecture.dtos.director.question.DirectorQuestionSearchCondition;
import com.gjgs.gjgs.modules.lecture.dtos.director.schedule.DirectorLectureScheduleResponse;
import com.gjgs.gjgs.modules.lecture.dtos.director.schedule.DirectorScheduleSearchCondition;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureQueryRepository;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureRepository;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureSearchQueryRepository;
import com.gjgs.gjgs.modules.utils.s3.FilePaths;
import com.gjgs.gjgs.modules.utils.s3.interfaces.AmazonS3Service;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DirectorLectureServiceImpl implements DirectorLectureService {

    private final FilePaths PATH = FilePaths.LECTURE_IMAGE_PATH;

    private final SecurityUtil securityUtil;
    private final LectureSearchQueryRepository lectureSearchQueryRepository;
    private final LectureQueryRepository lectureQueryRepository;
    private final LectureRepository lectureRepository;
    private final AmazonS3Service s3Service;

    @Override
    @Transactional(readOnly = true)
    public DirectorLectureResponse getDirectorLectures(GetLectureType type) {
        return lectureQueryRepository.findDirectorLectures(getCurrentUsername(), type).orElseThrow(() -> new DirectorHaveNotLectureException());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DirectorLectureScheduleResponse> getDirectorLecturesSchedules(Pageable pageable, DirectorScheduleSearchCondition condition) {
        return lectureSearchQueryRepository.findSchedulesByDirectorUsername(getCurrentUsername(), pageable, condition);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DirectorQuestionResponse> getDirectorQuestions(Pageable pageable, DirectorQuestionSearchCondition condition) {
        return lectureSearchQueryRepository.findQuestionsByDirectorUsername(getCurrentUsername(), pageable, condition);
    }

    @Override
    public void deleteRejectLecture(Long lectureId) {
        PutLectureResponse response = lectureQueryRepository.findRejectLectureById(lectureId).orElseThrow(() -> new NotRejectLectureException());
        deleteRejectLectureProcess(response);
    }

    private void deleteRejectLectureProcess(PutLectureResponse response) {
        deleteFiles(response);
        Long lectureId = response.getLectureId();
        lectureQueryRepository.deleteSchedulesByLectureId(lectureId);
        lectureQueryRepository.deleteCurriculumsByLectureId(lectureId);
        lectureQueryRepository.deleteFinishedProductsByLectureId(lectureId);
        lectureRepository.deleteById(lectureId);
    }

    private void deleteFiles(PutLectureResponse response) {
        List<String> allFilenames = response.getAllFilenames();
        allFilenames.forEach(filename ->
                s3Service.delete(PATH.getPath(), filename));
    }

    @Override
    public void changeLectureCreating(Long lectureId) {
        if (lectureQueryRepository.existCreatingLectureByUsername(getCurrentUsername())) {
            throw new ExistCreatingLectureException();
        }

        Lecture lecture = lectureQueryRepository.findRejectLectureEntityById(lectureId).orElseThrow(() -> new NotRejectLectureException());
        lecture.rewrite();
    }

    private String getCurrentUsername() {
        return securityUtil.getCurrentUsername().orElseThrow(() -> new MemberNotFoundException());
    }
}
