package com.gjgs.gjgs.modules.coupon.repositories;

import com.gjgs.gjgs.modules.coupon.dto.DirectorCouponResponse;
import com.gjgs.gjgs.modules.coupon.dto.QDirectorCouponResponse_CouponResponse;
import com.gjgs.gjgs.modules.coupon.entity.Coupon;
import com.gjgs.gjgs.modules.member.entity.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.gjgs.gjgs.modules.coupon.entity.QCoupon.coupon;
import static com.gjgs.gjgs.modules.lecture.entity.QLecture.lecture;
import static com.gjgs.gjgs.modules.lecture.enums.LectureStatus.ACCEPT;

@Repository
@RequiredArgsConstructor
public class CouponQueryRepositoryImpl implements CouponQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public Optional<Coupon> findByLectureId(Long lectureId) {
        return Optional.ofNullable(
                query.selectFrom(coupon)
                        .join(coupon.lecture, lecture)
                        .where(lecture.id.eq(lectureId), coupon.available.eq(true))
                        .fetchOne()
        );
    }

    @Override
    public Optional<Coupon> findByLectureIdDirectorUsername(Long lectureId, String username) {

        QMember director = new QMember("director");

        return Optional.ofNullable(
                query.selectFrom(coupon)
                        .join(coupon.lecture, lecture)
                        .join(lecture.director, director)
                        .where(lecture.id.eq(lectureId),  director.username.eq(username))
                        .fetchOne());
    }

    @Override
    public DirectorCouponResponse findByDirectorUsername(String username) {

        QMember director = new QMember("director");

        List<DirectorCouponResponse.CouponResponse> response = query
                .select(new QDirectorCouponResponse_CouponResponse(
                        lecture.id,
                        lecture.title,
                        coupon.issueDate,
                        coupon.closeDate,
                        coupon.discountPrice,
                        coupon.chargeCount,
                        coupon.receivePeople,
                        coupon.remainCount)
                ).from(coupon)
                .join(coupon.lecture, lecture)
                .join(lecture.director, director)
                .where(director.username.eq(username),
                        lecture.lectureStatus.eq(ACCEPT))
                .fetch();

        return DirectorCouponResponse.of(response);
    }

    @Override
    public Optional<Coupon> findWithLectureByLectureId(Long lectureId) {
        return Optional.ofNullable(
                query.selectFrom(coupon)
                        .join(coupon.lecture, lecture).fetchJoin()
                        .where(lecture.id.eq(lectureId))
                        .fetchOne()
        );
    }
}
