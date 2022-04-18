package com.gjgs.gjgs.modules.question.repository;

import com.gjgs.gjgs.modules.lecture.dtos.LectureQuestionsResponse;
import com.gjgs.gjgs.modules.lecture.dtos.QLectureQuestionsResponse;
import com.gjgs.gjgs.modules.member.dto.mypage.MyQuestionDto;
import com.gjgs.gjgs.modules.member.dto.mypage.QMyQuestionDto;
import com.gjgs.gjgs.modules.member.entity.QMember;
import com.gjgs.gjgs.modules.question.dto.QQuestionDetailResponse;
import com.gjgs.gjgs.modules.question.dto.QQuestionDetailResponse_AnswerDetail;
import com.gjgs.gjgs.modules.question.dto.QQuestionDetailResponse_QuestionDetail;
import com.gjgs.gjgs.modules.question.dto.QuestionDetailResponse;
import com.gjgs.gjgs.modules.question.entity.Question;
import com.gjgs.gjgs.modules.question.enums.QuestionStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.gjgs.gjgs.modules.lecture.entity.QLecture.lecture;
import static com.gjgs.gjgs.modules.member.entity.QMember.member;
import static com.gjgs.gjgs.modules.question.entity.QQuestion.question;


@Repository
@RequiredArgsConstructor
public class QuestionQueryRepositoryImpl implements QuestionQueryRepository {

    private final JPAQueryFactory query;

    public List<MyQuestionDto> findMyQuestionsByUsername(String username) {

        QMember director = new QMember("director");

        return query
                .select(new QMyQuestionDto(
                        question.id.as("questionId"),
                        lecture.title.as("classTitle"),
                        director.nickname.as("directorNickname"),
                        question.mainText,
                        question.replyText,
                        question.questionStatus,
                        question.createdDate
                ))
                .from(question)
                .join(question.lecture, lecture)
                .join(lecture.director, director)
                .join(question.member, member)
                .where(member.username.eq(username))
                .fetch();
    }

    @Override
    public Optional<QuestionDetailResponse> findWithLectureQuestioner(Long questionId) {

        QMember director = new QMember("director");
        QMember questioner = new QMember("questioner");

        return Optional.ofNullable(
                query
                        .select(new QQuestionDetailResponse(
                                question.id,
                                lecture.id,
                                question.questionStatus,
                                new QQuestionDetailResponse_QuestionDetail(
                                        questioner.nickname,
                                        question.createdDate,
                                        question.mainText,
                                        questioner.imageFileUrl
                                ),
                                new QQuestionDetailResponse_AnswerDetail(
                                        director.nickname,
                                        question.replyText,
                                        director.imageFileUrl))
                        )
                        .from(question)
                        .innerJoin(question.lecture, lecture)
                        .innerJoin(lecture.director, director)
                        .innerJoin(question.member, questioner)
                        .where(question.id.eq(questionId)).fetchOne());
    }

    @Override
    public Optional<Question> findWithLectureDirector(Long questionId) {
        return Optional.ofNullable(
                query.selectFrom(question)
                        .innerJoin(question.lecture, lecture).fetchJoin()
                        .innerJoin(lecture.director, member).fetchJoin()
                        .where(question.id.eq(questionId)).fetchOne()
        );
    }

    @Override
    public Page<LectureQuestionsResponse> findLectureQuestions(Long lectureId, Pageable pageable) {

        QMember questioner = new QMember("questioner");

        List<LectureQuestionsResponse> contents = query.select(new QLectureQuestionsResponse(
                question.id, questioner.id, questioner.nickname,
                questioner.imageFileUrl, question.createdDate,
                question.mainText, question.questionStatus.eq(QuestionStatus.COMPLETE)
        )).from(question)
                .join(question.lecture, lecture)
                .join(question.member, questioner)
                .where(lecture.id.eq(lectureId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long count = query.select(question)
                .from(question)
                .join(question.lecture, lecture)
                .where(lecture.id.eq(lectureId))
                .fetchCount();
        return new PageImpl<>(contents, pageable, count);
    }
}
