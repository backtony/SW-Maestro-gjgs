package com.gjgs.gjgs.modules.matching.event;


import com.gjgs.gjgs.modules.member.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class MatchingCompleteEvent {

    private final List<Member> MemberList;

    private final Long teamId;

}
