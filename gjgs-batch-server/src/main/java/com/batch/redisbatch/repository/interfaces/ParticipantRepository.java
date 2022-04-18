package com.batch.redisbatch.repository.interfaces;

import com.batch.redisbatch.domain.lecture.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
}
