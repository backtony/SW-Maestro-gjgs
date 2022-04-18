package com.gjgs.gjgs.modules.lecture.repositories.review;

import com.gjgs.gjgs.modules.lecture.dtos.review.ReviewResponse;
import com.gjgs.gjgs.modules.lecture.entity.Review;
import com.gjgs.gjgs.modules.member.dto.mypage.MyReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface ReviewQueryRepository {

    Page<MyReviewResponse> findMyReviews(String username, Pageable pageable);

    Optional<Review> findWithLectureByReviewIdDirectorUsername(Long reviewId, String directorUsername);

    Slice<ReviewResponse> findDirectorReviewsByDirectorId(Pageable pageable, Long directorId);

    Double findLectureReviewsScoreAvg(Long lectureId);
}
