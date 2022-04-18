package com.batch.redisbatch.repository.interfaces;

import com.batch.redisbatch.domain.TeamOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamOrderRepository extends JpaRepository<TeamOrder,Long> {
}
