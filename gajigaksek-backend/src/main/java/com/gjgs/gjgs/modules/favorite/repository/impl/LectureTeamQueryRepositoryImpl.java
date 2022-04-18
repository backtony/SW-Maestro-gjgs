package com.gjgs.gjgs.modules.favorite.repository.impl;


import com.gjgs.gjgs.modules.favorite.dto.LectureTeamDto;
import com.gjgs.gjgs.modules.favorite.dto.QLectureTeamDto;
import com.gjgs.gjgs.modules.favorite.repository.interfaces.LectureTeamQueryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.gjgs.gjgs.modules.favorite.entity.QLectureTeam.lectureTeam;
import static com.gjgs.gjgs.modules.lecture.entity.QLecture.lecture;


@Repository
@RequiredArgsConstructor
public class LectureTeamQueryRepositoryImpl implements LectureTeamQueryRepository {

    private final JPAQueryFactory query;

    public List<LectureTeamDto> findNotFinishedLectureByTeamId(Long teamId) {
        return query
                .select(new QLectureTeamDto(
                        lectureTeam.id.as("lectureTeamId"),
                        lectureTeam.lecture.id.as("lectureId"),
                        lectureTeam.lecture.thumbnailImageFileUrl,
                        lectureTeam.lecture.zone.id.as("zoneId"),
                        lectureTeam.lecture.title,
                        lectureTeam.lecture.price
                ))
                .from(lectureTeam)
                .join(lectureTeam.lecture, lecture)
                .where(lectureTeam.team.id.eq(teamId), lectureTeam.lecture.finished.eq(false))
                .fetch();
    }

    public List<Long> findTeamByLectureIdAndTeamIdList(Long lectureId, List<Long> teamIdList) {

        return query
                .select(lectureTeam.team.id)
                .from(lectureTeam)
                .where(lectureTeam.lecture.id.eq(lectureId), lectureTeam.team.id.in(teamIdList))
                .fetch();
    }
}
