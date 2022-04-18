package com.gjgs.gjgs.modules.lecture.services;

import com.gjgs.gjgs.modules.bulletin.dto.search.BulletinSearchResponse;
import com.gjgs.gjgs.modules.bulletin.repositories.BulletinRepository;
import com.gjgs.gjgs.modules.exception.lecture.LectureNotFoundException;
import com.gjgs.gjgs.modules.lecture.dtos.LectureDetailResponse;
import com.gjgs.gjgs.modules.lecture.dtos.LectureQuestionsResponse;
import com.gjgs.gjgs.modules.lecture.dtos.review.ReviewResponse;
import com.gjgs.gjgs.modules.lecture.dtos.search.LectureSearchCondition;
import com.gjgs.gjgs.modules.lecture.dtos.search.LectureSearchResponse;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.ElasticsearchLectureQueryRepository;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureQueryRepository;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureSearchQueryRepository;
import com.gjgs.gjgs.modules.question.repository.QuestionRepository;
import com.gjgs.gjgs.modules.utils.aop.LoginMemberFavoriteBulletin;
import com.gjgs.gjgs.modules.utils.aop.LoginMemberFavoriteLecture;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService {

    private final LectureSearchQueryRepository lectureSearchQueryRepository;
    private final LectureQueryRepository lectureQueryRepository;
    private final BulletinRepository bulletinRepository;
    private final ElasticsearchLectureQueryRepository elasticsearchLectureQueryRepository;
    private final QuestionRepository questionRepository;

    @LoginMemberFavoriteLecture
    @Override
    @Transactional(readOnly = true)
    public Page<LectureSearchResponse> searchLectures(Pageable pageable, LectureSearchCondition cond) {
        return elasticsearchLectureQueryRepository.getLectures(pageable, cond);
    }

    @LoginMemberFavoriteLecture
    @Override
    @Transactional(readOnly = true)
    public LectureDetailResponse getLecture(Long lectureId) {
        lectureQueryRepository.updateViewCount(lectureId);
        return lectureQueryRepository.findLectureDetail(lectureId)
                .orElseThrow(() -> new LectureNotFoundException());
    }

    @LoginMemberFavoriteBulletin
    @Override
    @Transactional(readOnly = true)
    public Page<BulletinSearchResponse> getBulletinsPickedLecture(Long lectureId, Pageable pageable) {
        return bulletinRepository.findLecturePickBulletins(lectureId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LectureQuestionsResponse> getLectureQuestions(Long lectureId, Pageable pageable) {
        return questionRepository.findLectureQuestions(lectureId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewResponse> getLectureReviews(Long lectureId, Pageable pageable) {
        return lectureSearchQueryRepository.findReviewsByLectureId(lectureId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<LectureSearchResponse> getDirectorLectures(Pageable pageable, Long directorId) {
        return lectureQueryRepository.findDirectorLecturesByDirectorId(pageable, directorId);
    }
}
