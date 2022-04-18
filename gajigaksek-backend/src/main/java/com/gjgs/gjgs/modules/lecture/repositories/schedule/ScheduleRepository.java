package com.gjgs.gjgs.modules.lecture.repositories.schedule;

import com.gjgs.gjgs.modules.lecture.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
