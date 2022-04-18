package com.gjgs.gjgs.modules.lecture.services.apply;

import com.gjgs.gjgs.modules.exception.schedule.ScheduleNotFoundException;
import com.gjgs.gjgs.modules.exception.schedule.ScheduleNotFoundOrNotExistParticipantException;
import com.gjgs.gjgs.modules.lecture.dtos.apply.ApplyLectureTeamRequest;
import com.gjgs.gjgs.modules.lecture.dtos.apply.ApplyLectureTeamResponse;
import com.gjgs.gjgs.modules.lecture.dtos.apply.ScheduleIdTeamIdDto;
import com.gjgs.gjgs.modules.lecture.entity.Schedule;
import com.gjgs.gjgs.modules.lecture.event.ApplyNotificationEvent;
import com.gjgs.gjgs.modules.lecture.event.ApplyRedisEvent;
import com.gjgs.gjgs.modules.lecture.repositories.participant.ParticipantJdbcRepository;
import com.gjgs.gjgs.modules.lecture.repositories.participant.ParticipantQueryRepository;
import com.gjgs.gjgs.modules.lecture.repositories.schedule.ScheduleQueryRepository;
import com.gjgs.gjgs.modules.lecture.services.apply.timepolicy.CheckTimeType;
import com.gjgs.gjgs.modules.lecture.services.apply.timepolicy.TimePolicyFactory;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.payment.dto.PayType;
import com.gjgs.gjgs.modules.payment.entity.TeamOrder;
import com.gjgs.gjgs.modules.payment.repository.TeamOrderRepository;
import com.gjgs.gjgs.modules.payment.service.order.OrderService;
import com.gjgs.gjgs.modules.team.entity.Team;
import com.gjgs.gjgs.modules.team.services.authority.TeamLeaderAuthorityCheckable;
import com.gjgs.gjgs.modules.utils.converter.CheckTimeTypeConverter;
import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplyScheduleTeamServiceImpl implements ApplyScheduleTeamService {

    private final PayType payType = PayType.TEAM;

    private final ParticipantJdbcRepository participantJdbcRepository;
    private final ScheduleQueryRepository scheduleQueryRepository;
    private final ParticipantQueryRepository participantQueryRepository;
    private final TimePolicyFactory timePolicyFactory;
    private final TeamLeaderAuthorityCheckable teamLeaderAuthorityCheckable;
    private final OrderService orderService;
    private final CheckTimeTypeConverter checkTimeTypeConverter;
    private final ApplicationEventPublisher eventPublisher;
    private final TeamOrderRepository teamOrderRepository;

    @Override
    public ApplyLectureTeamResponse apply(Long scheduleId, ApplyLectureTeamRequest request) {
        Schedule schedule = scheduleQueryRepository.findWithLectureParticipantsByLectureScheduleId(request.getLectureId(), scheduleId)
                .orElseThrow(() -> new ScheduleNotFoundException());

        CheckTimeType checkTimeType = checkTimeTypeConverter.getTimeTypeFromPayType(payType);
        timePolicyFactory.getPolicy(checkTimeType).checkCloseTime(schedule.getStartLocalDateTime());
        Team findTeam = teamLeaderAuthorityCheckable.findTeamMembers(request.getTeamId());
        schedule.addParticipants(findTeam.getAllMembers());
        participantJdbcRepository.insertParticipants(schedule);

        ScheduleIdTeamIdDto scheduleIdTeamIdDto = orderService.createOrder(schedule, findTeam);
        eventPublisher.publishEvent(new ApplyNotificationEvent(findTeam));
        eventPublisher.publishEvent(new ApplyRedisEvent(scheduleIdTeamIdDto));
        teamOrderRepository.save(TeamOrder.of(schedule, findTeam));
        return ApplyLectureTeamResponse.of(scheduleId);
    }

    @Override
    public void cancel(Long scheduleId, Long teamId) throws IamportResponseException, IOException {
        List<Member> teamMembers = teamLeaderAuthorityCheckable.findTeamMembers(teamId).getAllMembers();
        Schedule schedule = scheduleQueryRepository.findWithParticipantsById(scheduleId).orElseThrow(() -> new ScheduleNotFoundOrNotExistParticipantException());
        List<Long> participantIdList = schedule.cancelAndReturnParticipantIdList(teamMembers);
        participantQueryRepository.deleteParticipants(participantIdList);

        orderService.cancelAllTeamMembers(scheduleId, teamId);
        TeamOrder teamOrder = teamOrderRepository.findByScheduleIdTeamId(scheduleId, teamId);
        teamOrder.teamCancel();
    }
}
