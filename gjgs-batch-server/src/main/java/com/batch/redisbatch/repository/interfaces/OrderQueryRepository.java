package com.batch.redisbatch.repository.interfaces;

import com.batch.redisbatch.domain.Order;

import java.util.List;

public interface OrderQueryRepository {

    List<Order> findWithCouponRewardMemberByTeamIdScheduleId(Long teamId, Long scheduleId);
}
