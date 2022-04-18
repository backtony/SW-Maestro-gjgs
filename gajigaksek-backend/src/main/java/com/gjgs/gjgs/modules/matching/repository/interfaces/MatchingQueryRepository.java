package com.gjgs.gjgs.modules.matching.repository.interfaces;

import com.gjgs.gjgs.modules.matching.dto.MatchingRequest;
import com.gjgs.gjgs.modules.matching.dto.MemberFcmIncludeNicknameDto;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.List;

public interface MatchingQueryRepository {

    boolean existsByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<MemberFcmIncludeNicknameDto> findMemberFcmDtoByMatchForm(MatchingRequest matchingRequest);

}
