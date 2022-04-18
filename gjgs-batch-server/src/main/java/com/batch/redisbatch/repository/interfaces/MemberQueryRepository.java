package com.batch.redisbatch.repository.interfaces;

import com.batch.redisbatch.dto.MemberFcmDto;

import java.util.Optional;

public interface MemberQueryRepository {

    Optional<MemberFcmDto> findOnlyFcmById(Long Id);
}
