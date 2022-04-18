package com.gjgs.gjgs.modules.lecture.services.apply;

import com.gjgs.gjgs.modules.dummy.*;
import com.gjgs.gjgs.modules.lecture.dtos.apply.ApplyLectureTeamRequest;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.lecture.entity.Schedule;
import com.gjgs.gjgs.modules.lecture.event.ApplyNotificationEvent;
import com.gjgs.gjgs.modules.lecture.event.ApplyRedisEvent;
import com.gjgs.gjgs.modules.lecture.repositories.participant.ParticipantJdbcRepository;
import com.gjgs.gjgs.modules.lecture.repositories.participant.ParticipantQueryRepository;
import com.gjgs.gjgs.modules.lecture.repositories.schedule.ScheduleQueryRepository;
import com.gjgs.gjgs.modules.lecture.services.apply.timepolicy.CheckTimeType;
import com.gjgs.gjgs.modules.lecture.services.apply.timepolicy.EnableApplyTeamTimePolicy;
import com.gjgs.gjgs.modules.lecture.services.apply.timepolicy.TimePolicyFactory;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.payment.entity.TeamOrder;
import com.gjgs.gjgs.modules.payment.enums.TeamOrderStatus;
import com.gjgs.gjgs.modules.payment.repository.TeamOrderRepository;
import com.gjgs.gjgs.modules.payment.service.order.OrderService;
import com.gjgs.gjgs.modules.team.entity.Team;
import com.gjgs.gjgs.modules.team.services.authority.TeamLeaderAuthorityCheckable;
import com.gjgs.gjgs.modules.utils.converter.CheckTimeTypeConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplyScheduleTeamServiceImplTest {

    @Mock TeamLeaderAuthorityCheckable teamLeaderAuthorityCheckable;
    @Mock ParticipantJdbcRepository participantJdbcRepository;
    @Mock ScheduleQueryRepository scheduleQueryRepository;
    @Mock ParticipantQueryRepository participantQueryRepository;
    @Mock TimePolicyFactory timePolicyFactory;
    @Mock EnableApplyTeamTimePolicy teamTimePolicy;
    @Mock OrderService orderService;
    @Mock ApplicationEventPublisher eventPublisher;
    @Mock CheckTimeTypeConverter checkTimeTypeConverter;
    @Mock TeamOrderRepository teamOrderRepository;
    @InjectMocks ApplyScheduleTeamServiceImpl applyLectureTeamService;

    @Test
    @DisplayName("클래스 신청하기 / 팀 신청")
    void apply_test() throws Exception {

        // given
        Member leader = MemberDummy.createTestUniqueMember(1);
        Member member1 = MemberDummy.createTestUniqueMember(1);
        Member member2 = MemberDummy.createTestUniqueMember(1);
        Team team = TeamDummy.createTeamOfManyMembers(ZoneDummy.createZone(1L), leader, member1, member2);
        Member director = MemberDummy.createTestDirectorMember();
        Lecture lecture = LectureDummy.createLecture(1, director);
        Schedule schedule = ScheduleDummy.createSchedule(lecture);
        ApplyLectureTeamRequest request = ApplyLectureTeamRequest.builder().teamId(1L).lectureId(lecture.getId()).build();
        stubbingFindTeam(team);
        stubbingFindSchedule(lecture, schedule);
        stubbingCheckTimeTeamType();
        stubbingTeamPolicy();

        // when
        applyLectureTeamService.apply(schedule.getId(), request);

        // then
        assertAll(
                () -> verify(teamLeaderAuthorityCheckable).findTeamMembers(request.getTeamId()),
                () -> verify(scheduleQueryRepository).findWithLectureParticipantsByLectureScheduleId(lecture.getId(), schedule.getId()),
                () -> verify(participantJdbcRepository).insertParticipants(any()),
                () -> verify(orderService).createOrder(schedule, team),
                () -> verify(timePolicyFactory).getPolicy(CheckTimeType.TEAM),
                () -> assertEquals(schedule.getParticipantList().size(), 3),
                () -> assertEquals(schedule.getParticipantList().size(), 3),
                () -> verify(eventPublisher,times(1)).publishEvent(ArgumentMatchers.any(ApplyNotificationEvent.class)),
                () -> verify(eventPublisher,times(1)).publishEvent(ArgumentMatchers.any(ApplyRedisEvent.class)),
                () -> verify(teamOrderRepository).save(any())
        );
    }

    private void stubbingCheckTimeTeamType() {
        when(checkTimeTypeConverter.getTimeTypeFromPayType(any())).thenReturn(CheckTimeType.TEAM);
    }

    @Test
    @DisplayName("클래스 팀 신청 취소하기")
    void cancel_test() throws Exception {

        // given
        Member leader = MemberDummy.createTestUniqueMember(1);
        Member member1 = MemberDummy.createTestUniqueMember(1);
        Member member2 = MemberDummy.createTestUniqueMember(1);
        Team team = TeamDummy.createTeamOfManyMembers(ZoneDummy.createZone(1L), leader, member1, member2);
        stubbingFindTeam(team);
        Member director = MemberDummy.createTestDirectorMember();
        Lecture lecture = LectureDummy.createLecture(1, director);
        Schedule schedule = ScheduleDummy.createScheduleWithTeamParticipant(lecture, team);
        stubbingFindParticipantSchedule(schedule);
        stubbingFindTeamOrder(schedule, team);

        // when
        applyLectureTeamService.cancel(schedule.getId(), team.getId());

        // then
        assertAll(
                () -> verify(scheduleQueryRepository).findWithParticipantsById(schedule.getId()),
                () -> verify(teamLeaderAuthorityCheckable).findTeamMembers(team.getId()),
                () -> verify(participantQueryRepository).deleteParticipants(any()),
                () -> verify(orderService).cancelAllTeamMembers(schedule.getId(), team.getId()),
                () -> verify(teamOrderRepository).findByScheduleIdTeamId(schedule.getId(), team.getId()),
                () -> assertEquals(schedule.getCurrentParticipants(), 0),
                () -> assertEquals(schedule.getParticipantList().size(), 0)
        );
    }

    private void stubbingFindTeamOrder(Schedule schedule, Team team) {
        TeamOrder teamOrder = TeamOrder.builder()
                .teamOrderStatus(TeamOrderStatus.COMPLETE)
                .completePaymentCount(team.getCurrentMemberCount())
                .currentPaymentCount(team.getCurrentMemberCount())
                .scheduleId(schedule.getId())
                .teamId(team.getId())
                .build();
        when(teamOrderRepository.findByScheduleIdTeamId(schedule.getId(), team.getId()))
                .thenReturn(teamOrder);
    }

    private void stubbingTeamPolicy() {
        when(timePolicyFactory.getPolicy(CheckTimeType.TEAM)).thenReturn(teamTimePolicy);
    }

    private void stubbingFindParticipantSchedule(Schedule schedule) {
        when(scheduleQueryRepository.findWithParticipantsById(schedule.getId())).thenReturn(Optional.of(schedule));
    }

    private void stubbingFindSchedule(Lecture lecture, Schedule schedule) {
        when(scheduleQueryRepository.findWithLectureParticipantsByLectureScheduleId(lecture.getId(), schedule.getId())).thenReturn(Optional.of(schedule));
    }

    private void stubbingFindTeam(Team team) {
        when(teamLeaderAuthorityCheckable.findTeamMembers(any())).thenReturn(team);
    }
}