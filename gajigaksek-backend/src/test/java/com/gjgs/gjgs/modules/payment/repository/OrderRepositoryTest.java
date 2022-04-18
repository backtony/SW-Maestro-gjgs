package com.gjgs.gjgs.modules.payment.repository;

import com.gjgs.gjgs.config.repository.SetUpMemberRepository;
import com.gjgs.gjgs.modules.coupon.entity.Coupon;
import com.gjgs.gjgs.modules.coupon.repositories.CouponRepository;
import com.gjgs.gjgs.modules.coupon.repositories.MemberCouponRepository;
import com.gjgs.gjgs.modules.dummy.LectureDummy;
import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.dummy.ScheduleDummy;
import com.gjgs.gjgs.modules.dummy.TeamDummy;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.lecture.entity.Schedule;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureJdbcRepository;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureRepository;
import com.gjgs.gjgs.modules.lecture.repositories.participant.ParticipantJdbcRepository;
import com.gjgs.gjgs.modules.lecture.repositories.schedule.ScheduleRepository;
import com.gjgs.gjgs.modules.member.dto.mypage.CompleteLecturePaymentResponse;
import com.gjgs.gjgs.modules.member.dto.mypage.MyLectureResponse;
import com.gjgs.gjgs.modules.member.dto.mypage.TeamMemberPaymentStatusResponse;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.entity.MemberCoupon;
import com.gjgs.gjgs.modules.payment.dto.PaymentRequest;
import com.gjgs.gjgs.modules.payment.dto.TeamMemberPaymentResponse;
import com.gjgs.gjgs.modules.payment.entity.Order;
import com.gjgs.gjgs.modules.payment.enums.OrderStatus;
import com.gjgs.gjgs.modules.reward.entity.Reward;
import com.gjgs.gjgs.modules.reward.enums.RewardType;
import com.gjgs.gjgs.modules.reward.repository.interfaces.RewardRepository;
import com.gjgs.gjgs.modules.team.entity.Team;
import com.gjgs.gjgs.modules.team.repositories.interfaces.TeamJdbcRepository;
import com.gjgs.gjgs.modules.team.repositories.interfaces.TeamRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderRepositoryTest extends SetUpMemberRepository {

    @Autowired OrderJdbcRepository orderJdbcRepository;
    @Autowired LectureRepository lectureRepository;
    @Autowired LectureJdbcRepository lectureJdbcRepository;
    @Autowired ScheduleRepository scheduleRepository;
    @Autowired OrderRepository orderRepository;
    @Autowired OrderQueryRepository orderQueryRepository;
    @Autowired TeamRepository teamRepository;
    @Autowired TeamJdbcRepository teamJdbcRepository;
    @Autowired RewardRepository rewardRepository;
    @Autowired ParticipantJdbcRepository participantJdbcRepository;
    @Autowired MemberCouponRepository memberCouponRepository;
    @Autowired CouponRepository couponRepository;

    private Lecture lecture;
    private Team team;
    private Schedule schedule;
    private Reward reward;
    private MemberCoupon memberCoupon;
    Member member2;
    Member member3;
    Member member4;
    Member member5;

    void setUpTeamOrders() {
        member2 = anotherMembers.get(0);
        member3 = anotherMembers.get(1);
        member4 = anotherMembers.get(2);
        member5 = memberRepository.save(MemberDummy.createDataJpaTestMember(6, zone, category));
        lecture = lectureRepository.save(LectureDummy.createJdbcTestConfirmLecture(zone, category, director));
        lectureJdbcRepository.insertSchedule(lecture);
        schedule = scheduleRepository.save(ScheduleDummy.createSchedule(lecture));
        reward = Reward.builder().amount(1000).rewardType(RewardType.SAVE).text("test").member(member2).build();
        member2.addReward(reward.getAmount());
        rewardRepository.save(reward);
        Coupon coupon = couponRepository.save(Coupon.builder()
                .serialNumber(UUID.randomUUID().toString())
                .discountPrice(100).title("test coupon").remainCount(10)
                .chargeCount(10).available(true).receivePeople(0)
                .build());
        memberCoupon = memberCouponRepository.save(MemberCoupon.builder().serialNumber(coupon.getSerialNumber()).used(false).discountPrice(100).member(member2).build());
        team = teamRepository.save(TeamDummy.createTeamOfManyMembers(zone, leader, member2, member3, member4));
        teamJdbcRepository.insertMemberTeamList(team.getId(), List.of(member2.getId(), member3.getId(), member4.getId()));
        schedule.addParticipants(team.getAllMembers());
        participantJdbcRepository.insertParticipants(schedule);
        List<Order> orderList = Order.ofTeam(schedule, team);
        orderJdbcRepository.insertOrders(orderList);
        flushAndClear();
    }

    @Test
    @DisplayName("팀 주문 생성")
    void create_order_test() throws Exception {

        // given
        setUpTeamOrders();

        //when
        List<Order> findOrderList = orderRepository.findAll();

        // then
        assertEquals(findOrderList.size(), 4);
    }

    @Test
    @DisplayName("팀 한명의 결제 정보(임시) 가져오기")
    void find_by_lecture_schedule_id_username_test() throws Exception {

        // given
        setUpTeamOrders();

        // when
        Order order = orderQueryRepository
                .findByLectureScheduleIdUsername(lecture.getId(), schedule.getId(), leader.getUsername())
                .orElseThrow();

        // then
        assertAll(
                () -> assertEquals(order.getOrderStatus(), OrderStatus.WAIT),
                () -> assertNotNull(order.getId())
        );
    }

    @Test
    @DisplayName("팀에 속한 멤버가 신청한 스케줄의 정보(결제 정보) 가져오기")
    void get_team_member_payment_by_schedule_id_username_test() throws Exception {

        // given
        setUpTeamOrders();

        // when
        TeamMemberPaymentResponse response = orderQueryRepository
                .findTeamMemberPaymentByScheduleIdUsername(schedule.getId(), member2.getUsername())
                .orElseThrow();

        // then
        assertAll(
                () -> assertNotNull(response.getOrderId()),
                () -> assertNotNull(response.getTeamId()),
                () -> assertEquals(response.getHaveReward(), 1000)
        );
    }

    @Test
    @DisplayName("아직 결제가 완료되지 않은 팀을 Order에서 찾기")
    void exist_by_team_id_and_wait_test() throws Exception {

        // given
        setUpTeamOrders();

        // when, then
        assertTrue(orderQueryRepository.existWaitOrderTeamByTeamId(team.getId()));
    }

    @Test
    @DisplayName("마이페이지 나의 클래스 목록 조회하기")
    void find_my_lectures_test() throws Exception {

        // given
        setUpTeamOrders();
        PageRequest pageRequest = PageRequest.of(0, 10);

        // when
        Slice<MyLectureResponse> myLectures = orderQueryRepository.findMyAppliedLectures(pageRequest, leader.getUsername());

        // then
        assertAll(
                () -> assertEquals(myLectures.getContent().size(), 1),
                () -> assertTrue(myLectures.getContent().get(0).isLeader())
        );
    }

    @Test
    @DisplayName("마이페이지 나의 클래스 목록 조회하기 / 현재 속한 팀의 결제상황 조회하기")
    void find_order_status_by_team_id() throws Exception {

        // given
        setUpTeamOrders();

        // when
        TeamMemberPaymentStatusResponse response = orderQueryRepository.findOrderStatusByScheduleTeamIdUsername(schedule.getId(), team.getId(), leader.getUsername());

        // then
        assertEquals(response.getMemberStatusList().size(), 3);
    }

    @Test
    @DisplayName("마이페이지 나의 클래스 목록 조회하기 / 결제 완료 후 상세 페이지 조회")
    void find_payment_detail_by_id_test() throws Exception {

        // given
        setUpTeamOrders();
        Order testOrder = orderRepository.findAll().get(0);
        testOrder.applyPayment(PaymentRequest.builder().totalPrice(testOrder.getFinalPrice()).build());
        testOrder.complete();
        flushAndClear();

        // when
        CompleteLecturePaymentResponse response = orderQueryRepository.findPaymentDetailById(testOrder.getId()).orElseThrow();

        // then
        assertAll(
                () -> assertEquals(lecture.getId(), response.getLectureId()),
                () -> assertEquals(schedule.getId(), response.getScheduleId())
        );
    }

    @Test
    @DisplayName("쿠폰과 리워드가 적용된 경우 스케줄 정보까지 한번에 가져오기")
    void find_with_coupon_reward_by_id_test() throws Exception {

        // given
        setUpTeamOrders();
        Order order = orderRepository.findAll().get(0);
        order.addMemberCoupon(memberCoupon);
        order.addReward(reward);
        flushAndClear();

        // when
        Order findOrder = orderQueryRepository.findWithCouponRewardScheduleTeamById(order.getId()).orElseThrow();

        // then
        assertAll(
                () -> assertEquals(findOrder.getReward().getId(), reward.getId()),
                () -> assertEquals(findOrder.getMemberCoupon().getId(), memberCoupon.getId()),
                () -> assertEquals(findOrder.getSchedule().getId(), schedule.getId()),
                () -> assertEquals(findOrder.getTeam().getId(), team.getId()),
                () -> assertEquals(findOrder.getSchedule().getId(), schedule.getId())
        );
    }

    @Test
    @DisplayName("팀원들의 결제 정보를 모두 가져오기")
    void find_with_member_payment_by_schedule_team_id_test() throws Exception {

        // given
        setUpTeamOrders();

        // when
        List<Order> teamMemberOrders = orderQueryRepository.findWithMemberPaymentByScheduleTeamId(schedule.getId(), team.getId());

        // then
        assertEquals(teamMemberOrders.size(), 4);
    }

    @Test
    @DisplayName("결제정보에서 스케줄 및 스케줄 참가자 가져오기")
    void find_schedule_participants_by_id_test() throws Exception {

        // given
        setUpTeamOrders();
        List<Order> orders = orderRepository.findAll();
        Long orderId = orders.get(0).getId();

        // when
        Order findOrder = orderQueryRepository.findScheduleParticipantsById(orderId).orElseThrow();

        // then
        Schedule schedule = findOrder.getSchedule();
        assertAll(
                () -> assertEquals(schedule.getCurrentParticipants(), 4),
                () -> assertEquals(schedule.getParticipantList().size(), 4)
        );
    }

    @Test
    @DisplayName("orderId로 scheduleId 찾기")
    void find_ScheduleId_By_Id() throws Exception {

        // given
        setUpTeamOrders();
        List<Order> orders = orderRepository.findAll();
        Long orderId = orders.get(0).getId();

        // when, then
        assertNotNull(orderQueryRepository.findScheduleIdById(orderId));
    }
}
