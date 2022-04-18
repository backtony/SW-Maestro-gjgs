package com.batch.redisbatch.repository.interfaces;

import com.batch.redisbatch.config.QuerydslConfig;
import com.batch.redisbatch.domain.*;
import com.batch.redisbatch.domain.lecture.Lecture;
import com.batch.redisbatch.domain.lecture.Schedule;
import com.batch.redisbatch.dummy.LectureDummy;
import com.batch.redisbatch.dummy.MemberDummy;
import com.batch.redisbatch.dummy.ScheduleDummy;
import com.batch.redisbatch.enums.OrderStatus;
import com.batch.redisbatch.repository.impl.OrderQueryRepositoryImpl;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Import({QuerydslConfig.class, OrderQueryRepositoryImpl.class})
class OrderQueryRepositoryTest {

    @Autowired JPAQueryFactory queryFactory;
    @Autowired EntityManager em;
    @Autowired OrderQueryRepository orderQueryRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired CouponRepository couponRepository;
    @Autowired RewardRepository rewardRepository;
    @Autowired MemberCouponRepository memberCouponRepository;
    @Autowired OrderRepository orderRepository;
    @Autowired TeamRepository teamRepository;
    @Autowired ScheduleRepository scheduleRepository;
    @Autowired LectureRepository lectureRepository;
    @Autowired ZoneRepository zoneRepository;

    @Test
    @DisplayName("Order 조회 시 쿠폰과 리워드 같이 찾기")
    void get_order_with_coupon_reward() throws Exception {

        // given
        Zone zone = zoneRepository.save(createZone());
        Member member = memberRepository.save(MemberDummy.createMember(zone, 1));
        Member member2 = memberRepository.save(MemberDummy.createMember(zone, 2));
        Reward reward = rewardRepository.save(createReward(member));
        Coupon coupon = couponRepository.save(createCoupon());
        Lecture lecture = lectureRepository.save(LectureDummy.createLecture());
        Schedule schedule = scheduleRepository.save(ScheduleDummy.createSchedule(lecture));
        MemberCoupon memberCoupon = memberCouponRepository.save(createMemberCoupon(member, coupon));
        Team team = teamRepository.save(Team.builder().build());
        orderRepository.save(createOrder(member, memberCoupon, reward, schedule, team));
        orderRepository.save(createOrder(member2, memberCoupon, reward, schedule, team));
        flushAndClear();

        // when
        List<Order> orderList = orderQueryRepository
                .findWithCouponRewardMemberByTeamIdScheduleId(team.getId(), schedule.getId());

        // then
        assertEquals(orderList.size(), 2);
    }

    private Order createOrder(Member member, MemberCoupon memberCoupon, Reward reward, Schedule schedule, Team team) {
        return Order.builder()
                .schedule(schedule)
                .member(member)
                .orderStatus(OrderStatus.COMPLETE)
                .memberCoupon(memberCoupon)
                .reward(reward)
                .team(team)
                .build();
    }

    private MemberCoupon createMemberCoupon(Member member, Coupon coupon) {
        return MemberCoupon.builder()
                .serialNumber(coupon.getSerialNumber())
                .member(member)
                .build();
    }

    private Coupon createCoupon() {
        return Coupon.builder()
                .discountPrice(1000)
                .title("test")
                .remainCount(10)
                .chargeCount(10)
                .available(true)
                .receivePeople(0)
                .serialNumber(UUID.randomUUID().toString())
                .build();
    }

    private Reward createReward(Member member) {
        return Reward.builder()
                .member(member)
                .amount(2000)
                .text("test")
                .rewardType("use")
                .build();
    }

    private Zone createZone(){
        return Zone.builder().build();
    }

    public void flushAndClear() {
        em.flush();
        em.clear();
    }
}
