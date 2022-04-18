package com.batch.redisbatch.repository.interfaces;

import com.batch.redisbatch.domain.lecture.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
}
