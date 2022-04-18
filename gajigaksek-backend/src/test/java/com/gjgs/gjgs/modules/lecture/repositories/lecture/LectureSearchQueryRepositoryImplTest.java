package com.gjgs.gjgs.modules.lecture.repositories.lecture;

import com.gjgs.gjgs.config.repository.SetUpLectureTeamBulletinRepository;
import com.gjgs.gjgs.modules.lecture.dtos.review.CreateReviewRequest;
import com.gjgs.gjgs.modules.lecture.dtos.review.ReviewResponse;
import com.gjgs.gjgs.modules.lecture.entity.Review;
import com.gjgs.gjgs.modules.lecture.repositories.review.ReviewRepository;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.utils.vo.FileInfoVo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;

class LectureSearchQueryRepositoryImplTest extends SetUpLectureTeamBulletinRepository {

    @Autowired LectureSearchQueryRepository lectureSearchQueryRepository;
    @Autowired ReviewRepository reviewRepository;

    @Test
    @DisplayName("클래스의 리뷰 보기")
    void get_Lecture_Reviews() throws Exception {

        // given
        Member reviewer = anotherMembers.get(0);
        CreateReviewRequest request = CreateReviewRequest.builder().lectureId(lecture.getId()).score(5).text("test").build();
        reviewRepository.save(Review.of(reviewer, FileInfoVo.builder().build(), request));

        // when
        Page<ReviewResponse> response = lectureSearchQueryRepository.findReviewsByLectureId(lecture.getId(), PageRequest.of(0, 10));

        // then
        assertAll(
                () -> assertEquals(response.getContent().size(), 1),
                () -> assertNotNull(response.getContent().get(0).getReviewId())
        );
    }
}