package com.gjgs.gjgs.modules.lecture.services.review;

import com.gjgs.gjgs.modules.lecture.dtos.review.CreateReviewReplyRequest;
import com.gjgs.gjgs.modules.lecture.dtos.review.CreateReviewRequest;
import com.gjgs.gjgs.modules.lecture.dtos.review.ReviewResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ReviewService {

    void createReview(CreateReviewRequest request, MultipartFile file) throws IOException;

    void replyReview(Long reviewId, CreateReviewReplyRequest request);

    Slice<ReviewResponse> getDirectorsReviews(Pageable pageable, Long directorId);
}
