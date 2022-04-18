package com.gjgs.gjgs.modules.dummy;

import com.gjgs.gjgs.modules.matching.dto.MatchingRequest;

public class MatchingDummy {
    public static MatchingRequest createMatchingRequest(){
        return MatchingRequest.builder()
                .zoneId(1L)
                .categoryId(1L)
                .dayType("MON|TUE")
                .timeType("AFTERNOON")
                .preferMemberCount(4)
                .build();
    }
}
