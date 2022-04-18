package com.gjgs.gjgs.modules.lecture.services.review;

import com.gjgs.gjgs.modules.exception.lecture.LectureNotFoundException;
import com.gjgs.gjgs.modules.exception.member.MemberNotFoundException;
import com.gjgs.gjgs.modules.exception.review.NotExistReplyReviewException;
import com.gjgs.gjgs.modules.exception.schedule.NotScheduleParticipantException;
import com.gjgs.gjgs.modules.lecture.dtos.review.CreateReviewReplyRequest;
import com.gjgs.gjgs.modules.lecture.dtos.review.CreateReviewRequest;
import com.gjgs.gjgs.modules.lecture.dtos.review.ReviewResponse;
import com.gjgs.gjgs.modules.lecture.entity.Review;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureQueryRepository;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureRepository;
import com.gjgs.gjgs.modules.lecture.repositories.participant.ParticipantQueryRepository;
import com.gjgs.gjgs.modules.lecture.repositories.review.ReviewQueryRepository;
import com.gjgs.gjgs.modules.lecture.repositories.review.ReviewRepository;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberRepository;
import com.gjgs.gjgs.modules.utils.s3.FileManager;
import com.gjgs.gjgs.modules.utils.s3.FilePaths;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import com.gjgs.gjgs.modules.utils.vo.FileInfoVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.gjgs.gjgs.modules.lecture.enums.LectureStatus.ACCEPT;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final String savedPath = FilePaths.REVIEW_IMAGE_PATH.getPath();

    private final SecurityUtil securityUtil;
    private final ReviewQueryRepository reviewQueryRepository;
    private final LectureRepository lectureRepository;
    private final LectureQueryRepository lectureQueryRepository;
    private final ReviewRepository reviewRepository;
    private final FileManager fileManager;
    private final MemberRepository memberRepository;
    private final ParticipantQueryRepository participantQueryRepository;

    @Override
    public void createReview(CreateReviewRequest request, MultipartFile file) throws IOException {
        FileInfoVo fileInfo = saveFile(file);
        Member reviewer = validReviewerInParticipants(request);
        if (!lectureRepository.existsLectureByIdAndLectureStatus(request.getLectureId(), ACCEPT)) {
            throw new LectureNotFoundException();
        }
        reviewRepository.save(Review.of(reviewer, fileInfo, request));
        setLectureScore(request);
    }

    private FileInfoVo saveFile(MultipartFile file) throws IOException {
        if (file != null) {
            return fileManager.uploadFile(file, savedPath);
        }
        return FileInfoVo.empty();
    }

    private Member validReviewerInParticipants(CreateReviewRequest request) {
        Long memberId = memberRepository.findIdByUsername(getCurrentUsername()).orElseThrow(() -> new MemberNotFoundException());
        if (!participantQueryRepository.existParticipantByScheduleIdMemberId(request.getScheduleId(), memberId)) {
            throw new NotScheduleParticipantException();
        }
        return Member.from(memberId);
    }

    private String getCurrentUsername() {
        return securityUtil.getCurrentUsername().orElseThrow(() -> new MemberNotFoundException());
    }

    private void setLectureScore(CreateReviewRequest request) {
        Double scoreAvg = reviewQueryRepository.findLectureReviewsScoreAvg(request.getLectureId());
        lectureQueryRepository.updateLectureScore(request.getLectureId(), scoreAvg);
    }

    @Override
    public void replyReview(Long reviewId, CreateReviewReplyRequest request) {
        Review review = reviewQueryRepository.findWithLectureByReviewIdDirectorUsername(reviewId, securityUtil.getCurrentUsername().get()).orElseThrow(() -> new NotExistReplyReviewException());
        review.reply(request.getReplyText());
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<ReviewResponse> getDirectorsReviews(Pageable pageable, Long directorId) {
        return reviewQueryRepository.findDirectorReviewsByDirectorId(pageable, directorId);
    }
}
