package com.gjgs.gjgs.modules.lecture.repositories.review;

import com.gjgs.gjgs.modules.lecture.dtos.review.QReviewResponse;
import com.gjgs.gjgs.modules.lecture.dtos.review.ReviewResponse;
import com.gjgs.gjgs.modules.lecture.entity.Review;
import com.gjgs.gjgs.modules.member.dto.mypage.MyReviewResponse;
import com.gjgs.gjgs.modules.member.dto.mypage.QMyReviewResponse;
import com.gjgs.gjgs.modules.member.entity.QMember;
import com.gjgs.gjgs.modules.utils.querydsl.RepositorySliceHelper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.gjgs.gjgs.modules.lecture.entity.QLecture.lecture;
import static com.gjgs.gjgs.modules.lecture.entity.QReview.review;

@Repository
@RequiredArgsConstructor
public class ReviewQueryRepositoryImpl implements ReviewQueryRepository{

    private final JPAQueryFactory query;

    @Override
    public Page<MyReviewResponse> findMyReviews(String username, Pageable pageable) {
        QMember reviewer = new QMember("reviewer");

        List<MyReviewResponse> contents = query.select(new QMyReviewResponse(
                lecture.id, lecture.title,
                review.reviewImageFileUrl, review.text, review.replyText, review.score
        )).from(review)
                .join(review.member, reviewer)
                .join(review.lecture, lecture)
                .where(reviewer.username.eq(username))
                .fetch();

        long count = query.selectFrom(review).where(review.member.username.eq(username)).fetchCount();
        return new PageImpl<>(contents, pageable, count);
    }

    @Override
    public Optional<Review> findWithLectureByReviewIdDirectorUsername(Long reviewId, String directorUsername) {
        QMember director = new QMember("director");

        return Optional.ofNullable(query.selectFrom(review)
                .join(review.lecture, lecture)
                .join(lecture.director, director)
                .where(review.id.eq(reviewId),
                        director.username.eq(directorUsername))
                .fetchOne());
    }

    @Override
    public Slice<ReviewResponse> findDirectorReviewsByDirectorId(Pageable pageable, Long directorId) {
        QMember director = new QMember("director");
        QMember reviewer = new QMember("reviewer");

        List<ReviewResponse> contents = query.select(new QReviewResponse(
                review.id, review.reviewImageFileUrl, review.text,
                review.replyText, review.score, reviewer.nickname, reviewer.imageFileUrl
        )).from(review)
                .join(review.member, reviewer)
                .join(review.lecture, lecture)
                .join(lecture.director, director)
                .where(director.id.eq(directorId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return RepositorySliceHelper.toSlice(contents, pageable);
    }

    @Override
    public Double findLectureReviewsScoreAvg(Long lectureId) {
        return query.select(review.score.doubleValue().avg())
                .from(review)
                .join(review.lecture, lecture)
                .where(lecture.id.eq(lectureId))
                .fetchOne();
    }
}
