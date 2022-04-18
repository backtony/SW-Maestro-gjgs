package com.batch.redisbatch.repository.interfaces;

import com.batch.redisbatch.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team,Long> {
}
