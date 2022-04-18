package com.gjgs.gjgs.modules.lecture.controllers;

import com.gjgs.gjgs.modules.lecture.dtos.review.CreateReviewReplyRequest;
import com.gjgs.gjgs.modules.lecture.dtos.review.CreateReviewRequest;
import com.gjgs.gjgs.modules.lecture.dtos.review.ReviewResponse;
import com.gjgs.gjgs.modules.lecture.services.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PreAuthorize("hasAnyRole('USER,DIRECTOR')")
    @PostMapping(value = "", consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<Void> createReview(@RequestPart("request") @Validated CreateReviewRequest request,
                                             @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        reviewService.createReview(request, file);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{reviewId}")
    @PreAuthorize("hasAnyRole('DIRECTOR')")
    public ResponseEntity<Void> replyReview(@PathVariable Long reviewId,
                                            @RequestBody @Validated CreateReviewReplyRequest request) {
        reviewService.replyReview(reviewId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/directors/{directorId}")
    public ResponseEntity<Slice<ReviewResponse>> getDirectorsReviews(Pageable pageable,
                                                                     @PathVariable Long directorId) {
        return ResponseEntity.ok(reviewService.getDirectorsReviews(pageable, directorId));
    }
}
