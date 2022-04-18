package com.gjgs.gjgs.modules.lecture.repositories.participant;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.gjgs.gjgs.modules.lecture.entity.QParticipant.participant;
import static com.gjgs.gjgs.modules.lecture.entity.QSchedule.schedule;
import static com.gjgs.gjgs.modules.member.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class ParticipantQueryRepositoryImpl implements ParticipantQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public long deleteParticipants(List<Long> participantIdList) {
        return query.delete(participant)
                .where(participant.id.in(participantIdList)).execute();
    }

    @Override
    public long deleteParticipantsByScheduleIdMemberId(Long scheduleId, List<Long> memberIdList) {
        return query.delete(participant).
                where(participant.member.id.in(memberIdList),
                        participant.schedule.id.eq(scheduleId))
                .execute();
    }

    @Override
    public Boolean existParticipantByScheduleIdMemberId(Long scheduleId, Long memberId) {
        Integer fetchOne = query.selectOne()
                .from(participant)
                .join(participant.schedule, schedule)
                .join(participant.member, member)
                .where(schedule.id.eq(scheduleId), member.id.eq(memberId))
                .fetchFirst();

        return fetchOne != null;
    }
}
