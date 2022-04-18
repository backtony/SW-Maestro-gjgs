package com.gjgs.gjgs.modules.lecture.repositories.participant;

import java.util.List;

public interface ParticipantQueryRepository {

    long deleteParticipants(List<Long> participantIdList);

    long deleteParticipantsByScheduleIdMemberId(Long scheduleId, List<Long> memberId);

    Boolean existParticipantByScheduleIdMemberId(Long scheduleId, Long memberId);
}
