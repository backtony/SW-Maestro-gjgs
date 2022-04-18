package com.gjgs.gjgs.modules.lecture.repositories.participant;

import com.gjgs.gjgs.modules.lecture.entity.Schedule;

public interface ParticipantJdbcRepository {

    void insertParticipants(Schedule schedule);
}
