package com.batch.redisbatch.repository.interfaces;

import com.batch.redisbatch.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {
}
