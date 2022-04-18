package com.gjgs.gjgs.modules.lecture.repositories.participant;

import com.gjgs.gjgs.modules.lecture.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
}
