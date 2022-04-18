package com.gjgs.gjgs.modules.favorite.repository.interfaces;

import com.gjgs.gjgs.modules.favorite.entity.LectureMember;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LectureMemberRepository extends JpaRepository<LectureMember, Long> {

    @EntityGraph(attributePaths = "member")
    Optional<LectureMember> findLectureMemberWithMemberById(Long lectureMemberId);

    @Query("select lm from LectureMember lm where lm.member.id =:memberId and lm.lecture.id =:lectureId")
    Optional<LectureMember> findByMemberIdAndLectureId(@Param("memberId") Long memberId, @Param("lectureId") Long lectureId);

}
