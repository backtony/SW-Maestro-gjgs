package com.gjgs.gjgs.modules.payment.service.pay;

import com.gjgs.gjgs.modules.coupon.services.MemberCouponService;
import com.gjgs.gjgs.modules.dummy.LectureDummy;
import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.dummy.ScheduleDummy;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.lecture.entity.Schedule;
import com.gjgs.gjgs.modules.lecture.enums.ScheduleStatus;
import com.gjgs.gjgs.modules.lecture.repositories.participant.ParticipantJdbcRepository;
import com.gjgs.gjgs.modules.lecture.repositories.participant.ParticipantQueryRepository;
import com.gjgs.gjgs.modules.lecture.repositories.schedule.ScheduleQueryRepository;
import com.gjgs.gjgs.modules.lecture.services.apply.timepolicy.CheckTimeType;
import com.gjgs.gjgs.modules.lecture.services.apply.timepolicy.EnableApplyPersonalTimePolicy;
import com.gjgs.gjgs.modules.lecture.services.apply.timepolicy.TimePolicyFactory;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.entity.MemberCoupon;
import com.gjgs.gjgs.modules.payment.dto.PaymentRequest;
import com.gjgs.gjgs.modules.payment.entity.Order;
import com.gjgs.gjgs.modules.payment.repository.OrderQueryRepository;
import com.gjgs.gjgs.modules.payment.service.order.OrderService;
import com.gjgs.gjgs.modules.utils.converter.CheckTimeTypeConverter;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentPersonalProcessImplTest {

    @Mock ScheduleQueryRepository scheduleQueryRepository;
    @Mock ParticipantJdbcRepository participantJdbcRepository;
    @Mock TimePolicyFactory timePolicyFactory;
    @Mock OrderService orderService;
    @Mock MemberCouponService memberCouponService;
    @Mock EnableApplyPersonalTimePolicy enableApplyPersonalTimePolicy;
    @Mock SecurityUtil securityUtil;
    @Mock ParticipantQueryRepository participantQueryRepository;
    @Mock OrderQueryRepository orderQueryRepository;
    @Mock CheckTimeTypeConverter checkTimeTypeConverter;
    @InjectMocks PaymentPersonalProcessImpl paymentPersonalProcess;

    @Test
    @DisplayName("혼자 클래스를 신청할 경우 결제 까지 완료")
    void pay_process_test() throws Exception {

        // given
        Member director = MemberDummy.createTestDirectorMember();
        Lecture lecture = LectureDummy.createLecture(1, director);
        Schedule schedule = ScheduleDummy.createSchedule(lecture);
        stubbingFindWithLectureParticipantsById(lecture, schedule);

        MemberCoupon memberCoupon = MemberCoupon.builder().id(1L).build();
        Member findMember = Member.builder().coupons(List.of(memberCoupon)).build();
        PaymentRequest request = PaymentRequest.builder().memberCouponId(memberCoupon.getId()).lectureId(lecture.getId()).build();
        stubbingGetMemberOrWithCoupon(request, findMember);
        stubbingTimePolicyFactory();
        stubbingCheckTimePersonalType();

        // when
        paymentPersonalProcess.payProcess(schedule.getId(), request);

        // then
        assertAll(
                () -> verify(scheduleQueryRepository).findWithLectureParticipantsByLectureScheduleId(lecture.getId(), schedule.getId()),
                () -> verify(memberCouponService).getMemberOrWithCoupon(request),
                () -> verify(participantJdbcRepository).insertParticipants(schedule),
                () -> verify(orderService).paymentPersonal(findMember, schedule, request)
        );
    }

    private void stubbingCheckTimePersonalType() {
        when(checkTimeTypeConverter.getTimeTypeFromPayType(any())).thenReturn(CheckTimeType.PERSONAL);
    }

    @Test
    @DisplayName("개인 신청 취소하기 (결제 까지 취소)")
    void cancel_process_test()throws Exception {

        // given
        Member director = MemberDummy.createTestDirectorMember();
        Lecture lecture = LectureDummy.createLecture(1, director);
        Member participant = MemberDummy.createTestUniqueMember(1);
        stubbingCurrentMember(participant);
        Schedule schedule = ScheduleDummy.createScheduleWithParticipants(lecture, List.of(participant));
        Order order = Order.builder().id(1L).schedule(schedule).build();
        stubbingFindWithParticipant(order);

        // when
        paymentPersonalProcess.cancelProcess(order.getId());

        // then
        assertAll(
                () -> verify(orderQueryRepository).findScheduleParticipantsById(order.getId()),
                () -> verify(securityUtil).getCurrentUserOrThrow(),
                () -> verify(participantQueryRepository).deleteParticipants(any()),
                () -> verify(orderService).cancel(participant, order.getId()),
                () -> assertEquals(schedule.getCurrentParticipants(), 0),
                () -> assertEquals(schedule.getParticipantList().size(), 0),
                () -> assertEquals(schedule.getScheduleStatus(), ScheduleStatus.RECRUIT)
        );
    }

    private void stubbingCurrentMember(Member participant) {
        when(securityUtil.getCurrentUserOrThrow()).thenReturn(participant);
    }

    private void stubbingFindWithParticipant(Order order) {
        when(orderQueryRepository.findScheduleParticipantsById(any()))
                .thenReturn(Optional.of(order));
    }

    private void stubbingTimePolicyFactory() {
        when(timePolicyFactory.getPolicy(CheckTimeType.PERSONAL)).thenReturn(enableApplyPersonalTimePolicy);
    }

    private void stubbingGetMemberOrWithCoupon(PaymentRequest request, Member member) {
        when(memberCouponService.getMemberOrWithCoupon(request))
                .thenReturn(member);
    }

    private void stubbingFindWithLectureParticipantsById(Lecture lecture, Schedule schedule) {
        when(scheduleQueryRepository.findWithLectureParticipantsByLectureScheduleId(lecture.getId(), schedule.getId()))
                .thenReturn(Optional.of(schedule));
    }
}