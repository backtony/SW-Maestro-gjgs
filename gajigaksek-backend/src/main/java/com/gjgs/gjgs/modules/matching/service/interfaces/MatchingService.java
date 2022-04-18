package com.gjgs.gjgs.modules.matching.service.interfaces;


import com.gjgs.gjgs.modules.matching.dto.MatchingRequest;
import com.gjgs.gjgs.modules.matching.dto.MatchingStatusResponse;

public interface MatchingService {

    void matching(MatchingRequest matchingRequest);

    void cancel();

    MatchingStatusResponse status();
}
