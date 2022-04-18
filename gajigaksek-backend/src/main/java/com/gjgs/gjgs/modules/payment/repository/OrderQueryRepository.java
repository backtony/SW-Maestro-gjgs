package com.gjgs.gjgs.modules.payment.repository;

import com.gjgs.gjgs.modules.member.dto.mypage.CompleteLecturePaymentResponse;
import com.gjgs.gjgs.modules.member.dto.mypage.MyLectureResponse;
import com.gjgs.gjgs.modules.member.dto.mypage.TeamMemberPaymentStatusResponse;
import com.gjgs.gjgs.modules.payment.dto.TeamMemberPaymentResponse;
import com.gjgs.gjgs.modules.payment.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;

public interface OrderQueryRepository {

    Optional<Order> findByLectureScheduleIdUsername(Long lectureId, Long scheduleId, String username);

    Optional<TeamMemberPaymentResponse> findTeamMemberPaymentByScheduleIdUsername(Long scheduleId, String username);

    boolean existWaitOrderTeamByTeamId(Long teamId);

    Slice<MyLectureResponse> findMyAppliedLectures(Pageable pageable, String username);

    TeamMemberPaymentStatusResponse findOrderStatusByScheduleTeamIdUsername(Long scheduleId, Long teamId, String username);

    Optional<CompleteLecturePaymentResponse> findPaymentDetailById(Long orderId);

    Optional<Order> findWithCouponRewardScheduleTeamById(Long orderId);

    List<Order> findWithMemberPaymentByScheduleTeamId(Long scheduleId, Long teamId);

    Optional<Order> findScheduleParticipantsById(Long orderId);

    Optional<Long> findScheduleIdById(Long orderId);
}
