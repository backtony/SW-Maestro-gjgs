package com.batch.redisbatch.repository.impl;

import com.batch.redisbatch.domain.Order;
import com.batch.redisbatch.repository.interfaces.OrderQueryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.batch.redisbatch.domain.QMember.member;
import static com.batch.redisbatch.domain.QMemberCoupon.memberCoupon;
import static com.batch.redisbatch.domain.QOrder.order;
import static com.batch.redisbatch.domain.QReward.reward;
import static com.batch.redisbatch.domain.QTeam.team;
import static com.batch.redisbatch.domain.lecture.QSchedule.schedule;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepositoryImpl implements OrderQueryRepository {

    private final JPAQueryFactory query;

    public List<Order> findWithCouponRewardMemberByTeamIdScheduleId(Long teamId, Long scheduleId) {
        return query.selectFrom(order)
                .join(order.member, member).fetchJoin()
                .leftJoin(order.reward, reward).fetchJoin()
                .leftJoin(order.memberCoupon, memberCoupon).fetchJoin()
                .leftJoin(order.schedule, schedule).fetchJoin()
                .leftJoin(order.team, team)
                .where(team.id.eq(teamId), schedule.id.eq(scheduleId))
                .fetch();
    }
}
