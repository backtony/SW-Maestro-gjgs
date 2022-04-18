package com.gjgs.gjgs.modules.lecture.repositories.lecture;

import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.lecture.enums.LectureStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

    @Query("select l from Lecture l join l.director d where d.username = :username and l.id = :lectureId and l.lectureStatus = 'CREATING'")
    Optional<Lecture> findByIdUsername(@Param("lectureId") Long lectureId, @Param("username") String username);

    @Query("select l from Lecture l join l.director d where d.username =:username and l.lectureStatus = 'CREATING'")
    Optional<Lecture> findCreatingLectureByDirectorUsername(@Param("username") String username);

    boolean existsLectureByIdAndLectureStatus(Long lectureId,LectureStatus lectureStatus);
}
