package com.gjgs.gjgs.modules.favorite.repository.interfaces;

import com.gjgs.gjgs.modules.favorite.entity.LectureTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LectureTeamRepository extends JpaRepository<LectureTeam, Long> {

    @Query("select lt.id from LectureTeam lt where lt.lecture.id =:lectureId and lt.team.id =:teamId ")
    Optional<Long> findIdByLectureIdAndTeamId(@Param("lectureId") Long lectureId, @Param("teamId") Long teamId);
}
