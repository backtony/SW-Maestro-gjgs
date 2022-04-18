package com.gjgs.gjgs.modules.payment.repository;

import com.gjgs.gjgs.modules.member.dto.mypage.*;
import com.gjgs.gjgs.modules.member.entity.QMember;
import com.gjgs.gjgs.modules.payment.dto.QTeamMemberPaymentResponse;
import com.gjgs.gjgs.modules.payment.dto.TeamMemberPaymentResponse;
import com.gjgs.gjgs.modules.payment.entity.Order;
import com.gjgs.gjgs.modules.utils.querydsl.RepositorySliceHelper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.gjgs.gjgs.modules.lecture.entity.QLecture.lecture;
import static com.gjgs.gjgs.modules.lecture.entity.QParticipant.participant;
import static com.gjgs.gjgs.modules.lecture.entity.QReview.review;
import static com.gjgs.gjgs.modules.lecture.entity.QSchedule.schedule;
import static com.gjgs.gjgs.modules.member.entity.QMember.member;
import static com.gjgs.gjgs.modules.member.entity.QMemberCoupon.memberCoupon;
import static com.gjgs.gjgs.modules.payment.entity.QOrder.order;
import static com.gjgs.gjgs.modules.payment.enums.OrderStatus.COMPLETE;
import static com.gjgs.gjgs.modules.payment.enums.OrderStatus.WAIT;
import static com.gjgs.gjgs.modules.reward.entity.QReward.reward;
import static com.gjgs.gjgs.modules.team.entity.QTeam.team;
import static java.util.stream.Collectors.toList;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepositoryImpl implements OrderQueryRepository{

    private final JPAQueryFactory query;

    @Override
    public Optional<Order> findByLectureScheduleIdUsername(Long lectureId, Long scheduleId, String username) {
        return Optional.ofNullable(
                query.selectFrom(order)
                        .join(order.member, member)
                        .join(order.schedule, schedule)
                        .join(schedule.lecture, lecture)
                        .where(lecture.id.eq(lectureId),
                                schedule.id.eq(scheduleId),
                                member.username.eq(username))
                        .fetchOne()
        );
    }

    @Override
    public Optional<TeamMemberPaymentResponse> findTeamMemberPaymentByScheduleIdUsername(Long scheduleId, String username) {
        return Optional.ofNullable(
                query.select(new QTeamMemberPaymentResponse(
                        order.id, order.originalPrice,
                        schedule.id, schedule.lectureDate, schedule.startTime, schedule.endTime,
                        lecture.thumbnailImageFileUrl, lecture.title,
                        team.id, team.teamName,
                        member.totalReward
                ))
                        .from(order)
                        .join(order.schedule, schedule)
                        .join(schedule.lecture, lecture)
                        .join(order.member, member)
                        .join(order.team, team)
                        .where(member.username.eq(username),
                                schedule.id.eq(scheduleId),
                                order.orderStatus.eq(WAIT))
                        .fetchOne());
    }

    @Override
    public boolean existWaitOrderTeamByTeamId(Long teamId) {
        Integer fetchFirst = query.selectOne().from(order)
                .join(order.team, team)
                .where(team.id.eq(teamId), order.orderStatus.eq(WAIT))
                .fetchFirst();
        return fetchFirst != null;
    }

    @Override
    public Slice<MyLectureResponse> findMyAppliedLectures(Pageable pageable, String username) {

        QMember leader = new QMember("leader");
        QMember reviewer = new QMember("reviewer");

        List<MyLectureResponse> content = query.select(new QMyLectureResponse(
                order.id,
                lecture.id, lecture.thumbnailImageFileUrl, lecture.title,
                schedule.id, schedule.lectureDate, schedule.startTime, schedule.endTime,
                team.id, order.orderStatus, leader.username.eq(username)
        )).from(order)
                .leftJoin(order.team, team)
                .leftJoin(team.leader, leader)
                .join(order.member, member)
                .join(order.schedule, schedule)
                .join(schedule.lecture, lecture)
                .where(member.username.eq(username))
                .orderBy(order.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        List<Long> lectureIdList = content.stream().map(MyLectureResponse::getLectureId).collect(toList());
        List<Long> reviewedLectureIdList = query.select(lecture.id)
                .from(review)
                .join(review.member, reviewer)
                .join(review.lecture, lecture)
                .where(reviewer.username.eq(username),
                        lecture.id.in(lectureIdList))
                .fetch();

        content.forEach(myLectureResponse -> {
            if (reviewedLectureIdList.contains(myLectureResponse.getLectureId())) {
                myLectureResponse.setReviewed(true);
            }
        });

        return RepositorySliceHelper.toSlice(content, pageable);
    }

    @Override
    public TeamMemberPaymentStatusResponse findOrderStatusByScheduleTeamIdUsername(Long scheduleId, Long teamId, String username) {
        List<TeamMemberPaymentStatusResponse.MemberPaymentStatus> responseList = query.select(new QTeamMemberPaymentStatusResponse_MemberPaymentStatus(
                member.nickname, order.orderStatus
        )).from(order)
                .join(order.schedule, schedule)
                .join(order.member, member)
                .join(order.team, team)
                .where(team.id.eq(teamId),
                        schedule.id.eq(scheduleId),
                        member.username.ne(username))
                .fetch();
        return TeamMemberPaymentStatusResponse.of(responseList);
    }

    @Override
    public Optional<CompleteLecturePaymentResponse> findPaymentDetailById(Long orderId) {
        return Optional.ofNullable(
                query.select(new QCompleteLecturePaymentResponse(
                        lecture.id, lecture.thumbnailImageFileUrl, lecture.title, lecture.fullAddress,
                        schedule.id, schedule.lectureDate, schedule.startTime , schedule.endTime,
                        order.finalPrice
                )).from(order)
                .join(order.schedule, schedule)
                .join(schedule.lecture, lecture)
                .where(order.id.eq(orderId), order.orderStatus.eq(COMPLETE))
                .fetchOne()
        );
    }

    @Override
    public Optional<Order> findWithCouponRewardScheduleTeamById(Long orderId) {
        return Optional.ofNullable(
                query.selectFrom(order)
                        .leftJoin(order.reward, reward).fetchJoin()
                        .leftJoin(order.memberCoupon, memberCoupon).fetchJoin()
                        .join(order.schedule, schedule).fetchJoin()
                        .leftJoin(order.team).fetchJoin()
                        .where(order.id.eq(orderId)).fetchOne()
        );
    }

    @Override
    public List<Order> findWithMemberPaymentByScheduleTeamId(Long scheduleId, Long teamId) {
        return query.selectFrom(order)
                .leftJoin(order.memberCoupon, memberCoupon).fetchJoin()
                .leftJoin(order.reward, reward).fetchJoin()
                .join(order.member, member).fetchJoin()
                .join(order.schedule, schedule)
                .join(order.team, team)
                .where(schedule.id.eq(scheduleId), team.id.eq(teamId))
                .fetch();
    }

    @Override
    public Optional<Order> findScheduleParticipantsById(Long orderId) {
        return Optional.ofNullable(
                query.selectFrom(order)
                        .join(order.schedule, schedule).fetchJoin()
                        .leftJoin(schedule.participantList, participant).fetchJoin()
                        .join(participant.member, member).fetchJoin()
                        .where(order.id.eq(orderId))
                        .fetchOne()
        );
    }

    @Override
    public Optional<Long> findScheduleIdById(Long orderId) {
        return Optional.ofNullable(
                query.select(schedule.id)
                        .from(order)
                        .leftJoin(order.schedule, schedule)
                        .where(order.id.eq(orderId))
                        .fetchOne()
        );
    }
}
