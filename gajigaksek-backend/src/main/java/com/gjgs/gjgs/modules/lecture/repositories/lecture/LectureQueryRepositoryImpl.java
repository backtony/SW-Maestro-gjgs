package com.gjgs.gjgs.modules.lecture.repositories.lecture;

import com.gjgs.gjgs.modules.lecture.dtos.*;
import com.gjgs.gjgs.modules.lecture.dtos.create.*;
import com.gjgs.gjgs.modules.lecture.dtos.director.lecture.DirectorLectureResponse;
import com.gjgs.gjgs.modules.lecture.dtos.director.lecture.GetLectureType;
import com.gjgs.gjgs.modules.lecture.dtos.director.lecture.QDirectorLectureResponse;
import com.gjgs.gjgs.modules.lecture.dtos.director.lecture.QDirectorLectureResponse_LectureResponse;
import com.gjgs.gjgs.modules.lecture.dtos.director.schedule.DirectorScheduleResponse;
import com.gjgs.gjgs.modules.lecture.dtos.director.schedule.QDirectorScheduleResponse;
import com.gjgs.gjgs.modules.lecture.dtos.director.schedule.QDirectorScheduleResponse_ScheduleDto;
import com.gjgs.gjgs.modules.lecture.dtos.search.LectureSearchResponse;
import com.gjgs.gjgs.modules.lecture.dtos.search.QLectureSearchResponse;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.member.entity.QMember;
import com.gjgs.gjgs.modules.utils.querydsl.RepositorySliceHelper;
import com.querydsl.core.group.GroupBy;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.gjgs.gjgs.modules.category.entity.QCategory.category;
import static com.gjgs.gjgs.modules.coupon.entity.QCoupon.coupon;
import static com.gjgs.gjgs.modules.lecture.entity.QCurriculum.curriculum;
import static com.gjgs.gjgs.modules.lecture.entity.QFinishedProduct.finishedProduct;
import static com.gjgs.gjgs.modules.lecture.entity.QLecture.lecture;
import static com.gjgs.gjgs.modules.lecture.entity.QSchedule.schedule;
import static com.gjgs.gjgs.modules.lecture.enums.LectureStatus.*;
import static com.gjgs.gjgs.modules.lecture.enums.ScheduleStatus.RECRUIT;
import static com.gjgs.gjgs.modules.member.entity.QMember.member;
import static com.gjgs.gjgs.modules.zone.entity.QZone.zone;
import static com.querydsl.core.group.GroupBy.groupBy;

@Repository
@RequiredArgsConstructor
public class LectureQueryRepositoryImpl extends LectureRepositoryBooleanExpressionsProvider implements LectureQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public Optional<Lecture> findWithCategoryZoneByDirectorUsername(String username) {

        QMember director = new QMember("director");

        return Optional.ofNullable(
                query.selectFrom(lecture)
                        .join(lecture.director, director)
                        .join(lecture.zone, zone).fetchJoin()
                        .join(lecture.category, category).fetchJoin()
                        .where(
                                directorUsernameEq(username, director),
                                isCreating()).fetchOne());
    }

    @Override
    public Optional<LectureDetailResponse> findLectureDetail(Long lectureId) {
        QMember director = new QMember("director");

        return Optional.ofNullable(query.from(lecture)
                .join(lecture.director, director)
                .join(lecture.curriculumList, curriculum)
                .join(lecture.finishedProductList, finishedProduct)
                .join(lecture.scheduleList, schedule)
                .join(lecture.zone, zone)
                .join(lecture.category, category)
                .where(lecture.id.eq(lectureId))
                .orderBy(curriculum.orders.asc(),
                        finishedProduct.orders.asc(),
                        schedule.lectureDate.asc(),
                        schedule.startTime.asc())
                .transform(
                        groupBy(lecture.id).as(
                                new QLectureDetailResponse(
                                        lecture.id.as("lectureId"),
                                        lecture.thumbnailImageFileUrl.as("thumbnailImageUrl"),
                                        lecture.title.as("lectureTitle"),
                                        zone.id.as("zoneId"),
                                        category.id.as("categoryId"),
                                        lecture.progressTime.as("progressTime"),
                                        lecture.price.priceOne.as("priceOne"),
                                        lecture.price.priceTwo.as("priceTwo"),
                                        lecture.price.priceThree.as("priceThree"),
                                        lecture.price.priceFour.as("priceFour"),
                                        lecture.price.regularPrice.as("regularPrice"),
                                        lecture.mainText.as("mainText"),
                                        lecture.minParticipants.as("minParticipants"),
                                        lecture.maxParticipants.as("maxParticipants"),
                                        new QLectureDetailResponse_DirectorResponse(
                                                director.id.as("directorId"),
                                                director.profileText.as("directorProfileText"),
                                                director.imageFileUrl.as("directorProfileImageUrl")
                                        ),
                                        GroupBy.set(new QLectureDetailResponse_CurriculumResponse(
                                                curriculum.id,
                                                curriculum.orders,
                                                curriculum.title,
                                                curriculum.detailText,
                                                curriculum.curriculumImageUrl)),
                                        GroupBy.set(new QLectureDetailResponse_FinishedProductResponse(
                                                finishedProduct.id,
                                                finishedProduct.orders,
                                                finishedProduct.finishedProductImageUrl,
                                                finishedProduct.text)),
                                        GroupBy.set(new QLectureDetailResponse_ScheduleResponse(
                                                schedule.id,
                                                schedule.lectureDate,
                                                schedule.currentParticipants,
                                                schedule.startTime,
                                                schedule.endTime,
                                                schedule.progressMinutes,
                                                schedule.scheduleStatus))
                                )
                        )
                ).get(lectureId));
    }

    @Override
    public Optional<Lecture> findWithFinishedProductsByDirectorUsername(String username) {

        QMember director = new QMember("director");

        return Optional.ofNullable(query
                .selectFrom(lecture).distinct()
                        .leftJoin(lecture.finishedProductList, finishedProduct).fetchJoin()
                        .join(lecture.director, director)
                .where(directorUsernameEq(username, director),
                        isCreating())
                .fetchOne());
    }

    @Override
    public Optional<Lecture> findWithCurriculumsByDirectorUsername(String username) {

        QMember director = new QMember("director");

        return Optional.ofNullable(query
                .selectFrom(lecture).distinct()
                        .leftJoin(lecture.curriculumList, curriculum).fetchJoin()
                        .join(lecture.director, director)
                .where(directorUsernameEq(username, director),
                        isCreating())
                .fetchOne());
    }

    @Override
    public long deleteFinishedProductsByLectureId(Long lectureId) {
        return query.delete(finishedProduct)
                .where(finishedProduct.lecture.id.eq(lectureId))
                .execute();
    }

    @Override
    public long deleteCurriculumsByLectureId(Long lectureId) {
        return query.delete(curriculum)
                .where(curriculum.lecture.id.eq(lectureId))
                .execute();
    }

    @Override
    public Optional<Lecture> findWithSchedulesByDirectorUsername(String username) {

        QMember director = new QMember("director");

        return Optional.ofNullable(query
                .selectFrom(lecture).distinct()
                        .leftJoin(lecture.scheduleList, schedule).fetchJoin()
                        .join(lecture.director, director)
                .where(directorUsernameEq(username, director),
                        isCreating())
                .fetchOne());
    }

    @Override
    public long deleteSchedulesByLectureId(Long lectureId) {
        return query.delete(schedule)
                .where(schedule.lecture.id.eq(lectureId)).execute();
    }

    @Override
    public Optional<DirectorLectureResponse> findDirectorLectures(String username, GetLectureType type) {
        return Optional.ofNullable(query.from(lecture)
                .join(lecture.director, member)
                .where(directorUsernameEq(username, member), lectureStatusFinishedExpression(lecture, type))
                .orderBy(lecture.createdDate.desc())
                .transform(groupBy(member.username).as(new QDirectorLectureResponse(
                        GroupBy.list(new QDirectorLectureResponse_LectureResponse(
                                lecture.id,
                                lecture.thumbnailImageFileUrl,
                                lecture.title,
                                lecture.mainText,
                                lecture.lectureStatus,
                                lecture.finished,
                                lecture.rejectReason
                        ))
                ))).get(username));
    }

    @Override
    public Optional<Lecture> findAcceptLectureWithSchedulesByIdUsername(Long lectureId, String username) {

        QMember director = new QMember("director");

        return Optional.ofNullable(query.selectFrom(lecture).distinct()
                .join(lecture.director, director)
                .leftJoin(lecture.scheduleList, schedule).fetchJoin()
                .where(directorUsernameEq(username, director),
                        lecture.id.eq(lectureId),
                        isAccepted(lecture))
                .fetchOne());
    }

    @Override
    public Optional<DirectorScheduleResponse> findSchedulesByLectureIdUsername(Long lectureId, String username) {

        QMember director = new QMember("director");

        return Optional.ofNullable(
                query.from(lecture)
                .join(lecture.director, director)
                .leftJoin(lecture.scheduleList, schedule)
                .where(directorUsernameEq(username, director),
                        lecture.id.eq(lectureId),
                        isAccepted(lecture))
                .orderBy(schedule.createdDate.desc())
                .transform(groupBy(lecture.id).as(new QDirectorScheduleResponse(
                        lecture.id,
                        lecture.progressTime,
                        lecture.minParticipants,
                        lecture.maxParticipants,
                        GroupBy.set(new QDirectorScheduleResponse_ScheduleDto(
                                schedule.id,
                                schedule.lectureDate,
                                schedule.startTime,
                                schedule.endTime,
                                schedule.currentParticipants
                        ))
                ))).get(lectureId)
        );
    }

    @Override
    public Optional<Lecture> findWithScheduleByLectureScheduleId(Long lectureId, Long scheduleId) {
        return Optional.ofNullable(
                query.selectFrom(lecture)
                        .join(lecture.scheduleList, schedule).fetchJoin()
                .where(lecture.id.eq(lectureId),
                        isAccepted(lecture),
                        lecture.finished.eq(false),
                        schedule.id.eq(scheduleId),
                        schedule.scheduleStatus.eq(RECRUIT)).fetchOne()
        );
    }

    @Override
    public Optional<Lecture> findByIdDirectorUsername(Long lectureId, String username) {

        QMember director = new QMember("director");

        return Optional.ofNullable(
                query.selectFrom(lecture)
                        .join(lecture.director, director)
                        .where(directorUsernameEq(username, director),
                                lecture.id.eq(lectureId),
                                isAccepted(lecture))
                .fetchOne());
    }

    @Override
    public Optional<PutLectureResponse> findRejectLectureById(Long lectureId) {

        PutLectureResponse response = query.from(lecture)
                .join(lecture.category, category)
                .join(lecture.zone, zone)
                .leftJoin(lecture.finishedProductList, finishedProduct)
                .leftJoin(lecture.curriculumList, curriculum)
                .leftJoin(lecture.scheduleList, schedule)
                .where(
                        lecture.lectureStatus.eq(REJECT),
                        lecture.id.eq(lectureId)
                )
                .transform(groupBy(lecture.id).as(new QPutLectureResponse(
                        lecture.id, category.id, zone.id, lecture.title, lecture.fullAddress,
                        lecture.thumbnailImageFileName, lecture.thumbnailImageFileUrl,
                        lecture.minParticipants, lecture.maxParticipants,
                        lecture.mainText, lecture.lectureStatus,
                        GroupBy.set(new QPutLectureResponse_FinishedProductResponse(
                                finishedProduct.id, finishedProduct.orders, finishedProduct.text, finishedProduct.finishedProductImageName, finishedProduct.finishedProductImageUrl
                        )),
                        GroupBy.set(new QPutLectureResponse_CurriculumResponse(
                                curriculum.id, curriculum.orders, curriculum.title, curriculum.detailText, curriculum.curriculumImageName, curriculum.curriculumImageUrl
                        )),
                        GroupBy.set(new QPutLectureResponse_ScheduleResponse(
                                schedule.id, schedule.lectureDate, schedule.startTime, schedule.endTime, schedule.progressMinutes
                        )),
                        new QPutLectureResponse_PriceResponse(
                                lecture.price.regularPrice, lecture.price.priceOne, lecture.price.priceTwo,
                                lecture.price.priceThree, lecture.price.priceFour
                        )
                ))).get(lectureId);

        PutLectureResponse.CouponResponse couponResponse = query.select(new QPutLectureResponse_CouponResponse(
                coupon.discountPrice, coupon.chargeCount
        )).from(coupon)
                .join(coupon.lecture, lecture)
                .where(lecture.id.eq(lectureId),
                        lecture.lectureStatus.eq(REJECT))
                .fetchOne();
        response.setCoupon(couponResponse);
        return Optional.of(response);
    }

    @Override
    public boolean existCreatingLectureByUsername(String username) {
        QMember director = new QMember("director");

        Integer fetchFirst = query.selectOne()
                .from(lecture)
                .join(lecture.director, director)
                .where(director.username.eq(username),
                        lecture.lectureStatus.eq(CREATING))
                .fetchFirst();
        return fetchFirst != null;
    }

    @Override
    public Optional<Lecture> findRejectLectureEntityById(Long lectureId) {
        return Optional.ofNullable(
                query.selectFrom(lecture)
                        .where(lecture.id.eq(lectureId),
                                lecture.lectureStatus.eq(REJECT))
                        .fetchOne()
        );
    }

    @Override
    public Slice<LectureSearchResponse> findDirectorLecturesByDirectorId(Pageable pageable, Long directorId) {
        List<LectureSearchResponse> contents = query.select(new QLectureSearchResponse(
                lecture.id, lecture.onlyGjgs, lecture.thumbnailImageFileUrl,
                lecture.title, zone.id, lecture.price.priceOne, lecture.price.priceTwo,
                lecture.price.priceThree, lecture.price.priceFour
        )).from(lecture)
                .join(lecture.director, member)
                .join(lecture.zone, zone)
                .where(member.id.eq(directorId),
                        lecture.lectureStatus.eq(ACCEPT))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return RepositorySliceHelper.toSlice(contents, pageable);
    }

    @Override
    public Optional<Lecture> findWithDirectorById(Long lectureId) {
        QMember director = new QMember("director");

        return Optional.ofNullable(
                query.selectFrom(lecture)
                        .join(lecture.director, director).fetchJoin()
                        .where(lecture.id.eq(lectureId))
                        .fetchOne()
        );
    }

    @Override
    public long updateLectureScore(Long lectureId, Double scoreAvg) {
        return query.update(lecture)
                .set(lecture.score, scoreAvg)
                .where(lecture.id.eq(lectureId))
                .execute();
    }

    @Override
    public void updateViewCount(Long lectureId) {
        query.update(lecture)
                .set(lecture.clickCount, lecture.clickCount.add(1))
                .where(lecture.id.eq(lectureId))
                .execute();
    }
}
