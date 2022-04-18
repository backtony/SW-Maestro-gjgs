package com.gjgs.gjgs.modules.coupon.repositories;

import com.gjgs.gjgs.modules.coupon.dto.EnableMemberCouponResponse;
import com.gjgs.gjgs.modules.coupon.dto.QEnableMemberCouponResponse_EnableCoupon;
import com.gjgs.gjgs.modules.member.dto.mypage.MyAvailableCouponResponse;
import com.gjgs.gjgs.modules.member.dto.mypage.QMyAvailableCouponResponse_MyCoupon;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

import static com.gjgs.gjgs.modules.coupon.entity.QCoupon.coupon;
import static com.gjgs.gjgs.modules.lecture.entity.QLecture.lecture;
import static com.gjgs.gjgs.modules.member.entity.QMember.member;
import static com.gjgs.gjgs.modules.member.entity.QMemberCoupon.memberCoupon;
import static java.time.LocalDateTime.now;

@Repository
@RequiredArgsConstructor
public class MemberCouponQueryRepositoryImpl implements MemberCouponQueryRepository{

    private final JPAQueryFactory query;

    @Override
    public Optional<Member> findByMemberCouponByIdUsername(Long memberCouponId,
                                                           String username) {
        return Optional.ofNullable(
                query.selectFrom(member)
                        .join(member.coupons, memberCoupon).fetchJoin()
                        .where(memberCoupon.id.eq(memberCouponId),
                                member.username.eq(username),
                                memberCoupon.used.eq(false))
                        .fetchOne()
        );
    }

    @Override
    public EnableMemberCouponResponse findMyEnableCouponByLectureIdUsername(Long lectureId,
                                                                            String username) {
        LocalDateTime now = now();
        List<EnableMemberCouponResponse.EnableCoupon> enableCouponList = query.select(new QEnableMemberCouponResponse_EnableCoupon(
                memberCoupon.id,
                coupon.title,
                memberCoupon.discountPrice
        )).from(memberCoupon)
                .join(memberCoupon.member, member)
                .leftJoin(coupon).on(coupon.serialNumber.eq(memberCoupon.serialNumber))
                .leftJoin(coupon.lecture, lecture)
                .where(member.username.eq(username),
                        lecture.isNull().or(lecture.id.eq(lectureId)),
                        memberCoupon.used.eq(false),
                        coupon.issueDate.before(now),
                        coupon.closeDate.after(now))
                .fetch();
        return EnableMemberCouponResponse.of(new LinkedHashSet<>(enableCouponList));
    }

    @Override
    public MyAvailableCouponResponse findMyAvailableCouponsByUsername(String username) {

        LocalDateTime now = now();
        List<MyAvailableCouponResponse.MyCoupon> myCouponList = query.select(new QMyAvailableCouponResponse_MyCoupon(
                memberCoupon.id,
                coupon.title,
                memberCoupon.discountPrice,
                coupon.issueDate,
                coupon.closeDate
        )).from(memberCoupon)
                .join(memberCoupon.member, member)
                .innerJoin(coupon).on(coupon.serialNumber.eq(memberCoupon.serialNumber))
                .where(member.username.eq(username),
                        coupon.issueDate.before(now),
                        coupon.closeDate.after(now))
                .fetch();
        return MyAvailableCouponResponse.of(new LinkedHashSet<>(myCouponList));
    }
}
