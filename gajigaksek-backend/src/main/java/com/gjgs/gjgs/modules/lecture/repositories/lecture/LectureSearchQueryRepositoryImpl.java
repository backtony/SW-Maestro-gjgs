package com.gjgs.gjgs.modules.lecture.repositories.lecture;

import com.gjgs.gjgs.modules.lecture.dtos.director.question.*;
import com.gjgs.gjgs.modules.lecture.dtos.director.schedule.DirectorLectureScheduleResponse;
import com.gjgs.gjgs.modules.lecture.dtos.director.schedule.DirectorScheduleSearchCondition;
import com.gjgs.gjgs.modules.lecture.dtos.director.schedule.QDirectorLectureScheduleResponse;
import com.gjgs.gjgs.modules.lecture.dtos.review.QReviewResponse;
import com.gjgs.gjgs.modules.lecture.dtos.review.ReviewResponse;
import com.gjgs.gjgs.modules.member.entity.QMember;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.gjgs.gjgs.modules.lecture.entity.QLecture.lecture;
import static com.gjgs.gjgs.modules.lecture.entity.QReview.review;
import static com.gjgs.gjgs.modules.lecture.entity.QSchedule.schedule;
import static com.gjgs.gjgs.modules.question.entity.QQuestion.question;

@Repository
@RequiredArgsConstructor
public class LectureSearchQueryRepositoryImpl extends LectureRepositoryBooleanExpressionsProvider implements LectureSearchQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public Page<DirectorLectureScheduleResponse> findSchedulesByDirectorUsername(String username, Pageable pageable, DirectorScheduleSearchCondition condition) {
        QueryResults<DirectorLectureScheduleResponse> results = query.select(new QDirectorLectureScheduleResponse(
                lecture.id, schedule.id,
                lecture.title, schedule.currentParticipants,
                lecture.maxParticipants, schedule.lectureDate,
                schedule.startTime, schedule.endTime,
                lecture.price.regularPrice, lecture.price.priceOne, lecture.price.priceTwo,
                lecture.price.priceThree, lecture.price.priceFour, schedule.scheduleStatus
        )).from(lecture)
                .join(lecture.scheduleList, schedule)
                .where(
                        isAccepted(lecture),
                        containsKeyword(lecture, condition.getKeyword()),
                        scheduleStatusType(schedule, condition.getSearchType()),
                        dateGoeLoe(schedule, condition.getStartDate(), condition.getEndDate())
                )
                .orderBy(lecture.createdDate.desc(), schedule.createdDate.desc())
                .offset(pageable.getOffset()).limit(pageable.getPageSize()).fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    @Override
    public Page<DirectorQuestionResponse> findQuestionsByDirectorUsername(String username, Pageable pageable, DirectorQuestionSearchCondition condition) {

        QMember questioner = new QMember("questioner");
        QMember director = new QMember("director");

        QueryResults<DirectorQuestionResponse> results = query.select(new QDirectorQuestionResponse(
                new QDirectorQuestionResponse_LectureInfo(lecture.id, lecture.title, lecture.createdDate),
                new QDirectorQuestionResponse_QuestionInfo(question.id, questioner.id, questioner.nickname, question.mainText, question.replyText, question.questionStatus, question.createdDate)
        )).from(question)
                .join(question.lecture, lecture)
                .join(lecture.director, director)
                .join(question.member, questioner)
                .where(
                        directorUsernameEq(username, lecture.director),
                        answerStatusEq(question, condition.getQuestionStatus()),
                        lectureIdEq(lecture, condition.getLectureId())
                )
                .orderBy(question.createdDate.desc())
                .offset(pageable.getOffset()).limit(pageable.getPageSize()).fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    @Override
    public Page<ReviewResponse> findReviewsByLectureId(Long lectureId, Pageable pageable) {
        QMember reviewer = new QMember("reviewer");

        List<ReviewResponse> results = query.select(new QReviewResponse(
                review.id, review.reviewImageFileUrl, review.text, review.replyText,
                review.score, reviewer.nickname, reviewer.imageFileUrl
        )).from(lecture)
                .leftJoin(review).on(review.lecture.id.eq(lecture.id))
                .join(review.member, reviewer)
                .where(lecture.id.eq(lectureId))
                .orderBy(review.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long count = query.selectFrom(review).where(review.lecture.id.eq(lectureId)).fetchCount();
        return new PageImpl<>(results, pageable, count);
    }
}
