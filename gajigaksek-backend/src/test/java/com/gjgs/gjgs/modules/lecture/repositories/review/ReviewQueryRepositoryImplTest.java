package com.gjgs.gjgs.modules.lecture.repositories.review;

import com.gjgs.gjgs.config.repository.SetUpLectureTeamBulletinRepository;
import com.gjgs.gjgs.modules.lecture.dtos.review.ReviewResponse;
import com.gjgs.gjgs.modules.lecture.entity.Review;
import com.gjgs.gjgs.modules.member.dto.mypage.MyReviewResponse;
import com.gjgs.gjgs.modules.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ReviewQueryRepositoryImplTest extends SetUpLectureTeamBulletinRepository {

    @Autowired ReviewQueryRepository reviewQueryRepository;
    @Autowired ReviewRepository reviewRepository;

    @Test
    @DisplayName("내가 작성한 리뷰 가져오기")
    void find_my_reviews_test() throws Exception {

        // given
        Member reviewer1 = anotherMembers.get(0);
        Review review1 = Review.builder().lecture(lecture).text("testtest").member(reviewer1).score(4).build();
        reviewRepository.save(review1);
        PageRequest page = PageRequest.of(0, 10);

        // when
        Page<MyReviewResponse> response = reviewQueryRepository.findMyReviews(reviewer1.getUsername(), page);

        // then
        assertEquals(response.getTotalElements(), 1);
    }

    @Test
    @DisplayName("디렉터가 답글을 작성할 리뷰 가져오기")
    void find_with_lecture_by_review_id_director_username_test() throws Exception {

        // given
        PageRequest page = PageRequest.of(0, 10);
        Review review = reviewRepository.save(Review.builder().lecture(lecture).text("testtest").member(anotherMembers.get(0)).score(4).build());
        flushAndClear();

        // when
        Review findReview = reviewQueryRepository.findWithLectureByReviewIdDirectorUsername(review.getId(), director.getUsername()).orElseThrow();

        // then
        assertAll(
                () -> assertEquals(findReview.getLecture().getDirector().getUsername(), director.getUsername()),
                () -> assertEquals(findReview.getId(), review.getId())
        );
    }

    @Test
    @DisplayName("디렉터가 운영하는 클래스의 모든 리뷰 가져오기")
    void find_director_reviews_by_director_id_test()throws Exception {

        // given
        setUpReview();
        PageRequest pageRequest = PageRequest.of(0, 10);

        // when
        Slice<ReviewResponse> response = reviewQueryRepository.findDirectorReviewsByDirectorId(pageRequest, director.getId());

        // then
        assertEquals(response.getContent().size(), 2);
    }

    @Test
    @DisplayName("리뷰 평점 가져오기")
    void get_Lecture_Review_Score() throws Exception {

        // given
        setUpReview();
        flushAndClear();

        // when
        Double score = reviewQueryRepository.findLectureReviewsScoreAvg(lecture.getId());

        // then
        assertEquals(score, 2.5);
    }

    private void setUpReview() {
        Member reviewer1 = anotherMembers.get(0);
        Member reviewer2 = anotherMembers.get(1);
        Review review1 = Review.builder().lecture(lecture).text("testtest").member(reviewer1).score(4).build();
        Review review2 = Review.builder().lecture(lecture).text("testtest").member(reviewer2).score(1).build();
        reviewRepository.save(review1);
        reviewRepository.save(review2);
    }
}