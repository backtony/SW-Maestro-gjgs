package com.batch.redisbatch.repository.interfaces;

import java.util.List;

public interface ParticipantQueryRepository {

    long deleteParticipantsByScheduleIdMemberId(Long scheduleId, List<Long> memberIdList);
}
