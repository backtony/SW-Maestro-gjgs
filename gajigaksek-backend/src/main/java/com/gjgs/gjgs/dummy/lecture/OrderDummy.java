package com.gjgs.gjgs.dummy.lecture;

import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.lecture.entity.Schedule;
import com.gjgs.gjgs.modules.lecture.enums.ScheduleStatus;
import com.gjgs.gjgs.modules.lecture.repositories.participant.ParticipantJdbcRepository;
import com.gjgs.gjgs.modules.lecture.repositories.schedule.ScheduleRepository;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.payment.entity.Order;
import com.gjgs.gjgs.modules.payment.repository.OrderJdbcRepository;
import com.gjgs.gjgs.modules.team.entity.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.gjgs.gjgs.modules.payment.enums.OrderStatus.WAIT;
import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
@Profile("dev")
public class OrderDummy {

    private final OrderJdbcRepository orderJdbcRepository;
    private final ParticipantJdbcRepository participantJdbcRepository;
    private final ScheduleRepository scheduleRepository;

    public void createScheduleOrder(Team team, List<Member> allMembers, Lecture lecture) {
        createRecruitScheduleOrder(team, allMembers, lecture);
        createEndScheduleOrder(team, allMembers, lecture);
    }

    private void createRecruitScheduleOrder(Team team, List<Member> allMembers, Lecture lecture) {
        Schedule recruit = scheduleRepository.save(createRecruitSchedule(lecture));
        recruit.addParticipants(allMembers);
        participantJdbcRepository.insertParticipants(recruit);
        List<Order> orders = createTeamOrders(recruit, team, allMembers);
        orderJdbcRepository.insertOrders(orders);
    }

    private void createEndScheduleOrder(Team team, List<Member> allMembers, Lecture lecture) {
        Schedule recruit = scheduleRepository.save(createEndSchedule(lecture));
        recruit.addParticipants(allMembers);
        participantJdbcRepository.insertParticipants(recruit);
        List<Order> orders = createTeamOrders(recruit, team, allMembers);
        orderJdbcRepository.insertOrders(orders);
        recruit.setEnd();
    }

    private List<Order> createTeamOrders(Schedule schedule, Team team, List<Member> members) {
        return members.stream().map(member -> Order.builder()
                .member(member).schedule(schedule)
                .orderStatus(WAIT)
                .team(team)
                .originalPrice(schedule.getLecturePrice(team.getCurrentMemberCount()))
                .build()).collect(toList());
    }

    private Schedule createRecruitSchedule(Lecture lecture) {
        return Schedule.builder()
                .scheduleStatus(ScheduleStatus.RECRUIT)
                .lecture(lecture)
                .lectureDate(LocalDate.now().plusDays(2L))
                .startTime(LocalTime.of(15, 0))
                .endTime(LocalTime.of(17, 0))
                .progressMinutes(120).currentParticipants(0)
                .build();
    }
    private Schedule createEndSchedule(Lecture lecture) {
        return Schedule.builder()
                .scheduleStatus(ScheduleStatus.RECRUIT)
                .lecture(lecture)
                .lectureDate(LocalDate.now().minusDays(2L))
                .startTime(LocalTime.of(15, 0))
                .endTime(LocalTime.of(17, 0))
                .progressMinutes(120).currentParticipants(0)
                .build();
    }
}
