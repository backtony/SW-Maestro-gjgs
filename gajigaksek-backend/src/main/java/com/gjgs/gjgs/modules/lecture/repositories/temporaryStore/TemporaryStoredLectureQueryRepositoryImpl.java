package com.gjgs.gjgs.modules.lecture.repositories.temporaryStore;

import com.gjgs.gjgs.modules.lecture.dtos.admin.ConfirmLectureResponse;
import com.gjgs.gjgs.modules.lecture.dtos.admin.QConfirmLectureResponse;
import com.gjgs.gjgs.modules.lecture.dtos.create.*;
import com.gjgs.gjgs.modules.member.entity.QMember;
import com.querydsl.core.QueryResults;
import com.querydsl.core.group.GroupBy;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.gjgs.gjgs.modules.category.entity.QCategory.category;
import static com.gjgs.gjgs.modules.coupon.entity.QCoupon.coupon;
import static com.gjgs.gjgs.modules.lecture.entity.QCurriculum.curriculum;
import static com.gjgs.gjgs.modules.lecture.entity.QFinishedProduct.finishedProduct;
import static com.gjgs.gjgs.modules.lecture.entity.QLecture.lecture;
import static com.gjgs.gjgs.modules.lecture.entity.QSchedule.schedule;
import static com.gjgs.gjgs.modules.lecture.enums.LectureStatus.CONFIRM;
import static com.gjgs.gjgs.modules.lecture.enums.LectureStatus.CREATING;
import static com.gjgs.gjgs.modules.member.entity.QMember.member;
import static com.gjgs.gjgs.modules.zone.entity.QZone.zone;
import static com.querydsl.core.group.GroupBy.groupBy;

@Repository
@RequiredArgsConstructor
public class TemporaryStoredLectureQueryRepositoryImpl implements TemporaryStoredLectureQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public Optional<PutLectureResponse> findByDirectorUsername(String username) {

        QMember director = new QMember("director");

        PutLectureResponse response = query.from(lecture)
                .join(lecture.director, member)
                .join(lecture.category, category)
                .join(lecture.zone, zone)
                .leftJoin(lecture.finishedProductList, finishedProduct)
                .leftJoin(lecture.curriculumList, curriculum)
                .leftJoin(lecture.scheduleList, schedule)
                .where(
                        lecture.lectureStatus.eq(CREATING),
                        member.username.eq(username)
                )
                .transform(groupBy(member.username).as(new QPutLectureResponse(
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
                ))).get(username);

        PutLectureResponse.CouponResponse couponResponse = query.select(new QPutLectureResponse_CouponResponse(
                coupon.discountPrice, coupon.chargeCount
        )).from(coupon)
                .join(coupon.lecture, lecture)
                .join(lecture.director, director)
                .where(director.username.eq(username),
                        lecture.id.eq(response.getLectureId()))
                .fetchOne();
        response.setCoupon(couponResponse);
        return Optional.of(response);
    }

    @Override
    public Page<ConfirmLectureResponse> findConfirmLectures(Pageable pageable) {

        QMember director = new QMember("director");

        QueryResults<ConfirmLectureResponse> results = query.select(new QConfirmLectureResponse(
                lecture.id, lecture.title, lecture.lastModifiedDate,
                director.nickname,
                category.id, category.mainCategory.concat("|").concat(category.subCategory),
                zone.id, zone.mainZone.concat("|").concat(zone.subZone)
        ))
                .from(lecture)
                .join(lecture.director, director)
                .join(lecture.category, category)
                .join(lecture.zone, zone)
                .where(lecture.lectureStatus.eq(CONFIRM))
                .orderBy(lecture.createdDate.desc()).fetchResults();

        long count = query.selectFrom(lecture).where(lecture.lectureStatus.eq(CONFIRM)).fetchCount();
        return new PageImpl<>(results.getResults(), pageable, count);
    }

    @Override
    public Optional<PutLectureResponse> findConfirmLectureById(Long lectureId) {

        QMember director = new QMember("director");

        PutLectureResponse response = query.from(lecture)
                .join(lecture.director, member)
                .join(lecture.category, category)
                .join(lecture.zone, zone)
                .leftJoin(lecture.finishedProductList, finishedProduct)
                .leftJoin(lecture.curriculumList, curriculum)
                .leftJoin(lecture.scheduleList, schedule)
                .where(
                        lecture.lectureStatus.eq(CONFIRM),
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
                .join(lecture.director, director)
                .where(lecture.id.eq(lectureId),
                        lecture.lectureStatus.eq(CONFIRM))
                .fetchOne();
        response.setCoupon(couponResponse);
        return Optional.of(response);
    }
}
