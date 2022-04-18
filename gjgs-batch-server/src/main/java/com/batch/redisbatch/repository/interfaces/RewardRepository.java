package com.batch.redisbatch.repository.interfaces;

import com.batch.redisbatch.domain.Reward;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RewardRepository extends JpaRepository<Reward, Long> {
}
