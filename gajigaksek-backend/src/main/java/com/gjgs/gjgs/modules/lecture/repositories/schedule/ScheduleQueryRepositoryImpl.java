package com.gjgs.gjgs.modules.lecture.repositories.schedule;

import com.gjgs.gjgs.modules.lecture.entity.Schedule;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.gjgs.gjgs.modules.lecture.entity.QLecture.lecture;
import static com.gjgs.gjgs.modules.lecture.entity.QParticipant.participant;
import static com.gjgs.gjgs.modules.lecture.entity.QSchedule.schedule;
import static com.gjgs.gjgs.modules.member.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class ScheduleQueryRepositoryImpl implements ScheduleQueryRepository{

    private final JPAQueryFactory query;

    @Override
    public Optional<Schedule> findWithParticipantsById(Long scheduleId) {
        return Optional.ofNullable(
                query.selectFrom(schedule).distinct()
                        .innerJoin(schedule.participantList, participant).fetchJoin()
                        .innerJoin(participant.member, member).fetchJoin()
                        .where(schedule.id.eq(scheduleId))
                        .fetchOne()
        );
    }

    @Override
    public Optional<Schedule> findWithLectureParticipantsByLectureScheduleId(Long lectureId, Long scheduleId) {
        return Optional.ofNullable(
                query.selectFrom(schedule).distinct()
                        .join(schedule.lecture, lecture).fetchJoin()
                        .leftJoin(schedule.participantList, participant).fetchJoin()
                        .where(schedule.id.eq(scheduleId),
                                lecture.id.eq(lectureId)).fetchOne()
        );
    }
}
