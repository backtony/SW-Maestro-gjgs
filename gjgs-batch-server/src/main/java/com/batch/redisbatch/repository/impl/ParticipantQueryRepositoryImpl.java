package com.batch.redisbatch.repository.impl;

import com.batch.redisbatch.repository.interfaces.ParticipantQueryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.batch.redisbatch.domain.lecture.QParticipant.participant;

@Repository
@RequiredArgsConstructor
public class ParticipantQueryRepositoryImpl implements ParticipantQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public long deleteParticipantsByScheduleIdMemberId(Long scheduleId, List<Long> memberIdList) {
        return query.delete(participant).
                where(participant.member.id.in(memberIdList),
                        participant.schedule.id.eq(scheduleId))
                .execute();
    }
}
