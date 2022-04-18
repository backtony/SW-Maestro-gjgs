package com.gjgs.gjgs.modules.lecture.repositories.lecture;

import com.gjgs.gjgs.modules.lecture.entity.Lecture;

public interface LectureJdbcRepository {

    void insertFinishedProduct(Lecture lecture);

    void insertCurriculum(Lecture lecture);

    void insertSchedule(Lecture lecture);
}
