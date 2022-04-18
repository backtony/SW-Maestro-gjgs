package com.batch.redisbatch.repository.interfaces;

import com.batch.redisbatch.domain.lecture.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
