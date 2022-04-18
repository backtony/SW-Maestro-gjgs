package com.gjgs.gjgs.modules.payment.service.order;

import com.gjgs.gjgs.modules.lecture.dtos.apply.ScheduleIdTeamIdDto;
import com.gjgs.gjgs.modules.lecture.entity.Schedule;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.payment.dto.OrderIdDto;
import com.gjgs.gjgs.modules.payment.dto.PaymentRequest;
import com.gjgs.gjgs.modules.payment.dto.PaymentVerifyRequest;
import com.gjgs.gjgs.modules.payment.dto.TeamMemberPaymentResponse;
import com.gjgs.gjgs.modules.team.entity.Team;
import com.siot.IamportRestClient.exception.IamportResponseException;

import java.io.IOException;

public interface OrderService {

    ScheduleIdTeamIdDto createOrder(Schedule schedule, Team team);

    TeamMemberPaymentResponse getTeamMemberPayment(Long scheduleId);

    Long paymentPersonal(Member member, Schedule schedule, PaymentRequest paymentRequest);

    Long paymentTeamMember(Member member, Long scheduleId, PaymentRequest paymentRequest);

    OrderIdDto verifyAndCompletePayment(PaymentVerifyRequest request) throws IamportResponseException, IOException;

    void cancel(Member exitMember, Long orderId) throws IamportResponseException, IOException;

    void cancelAllTeamMembers(Long scheduleId, Long teamId) throws IamportResponseException, IOException;
}
