package com.gjgs.gjgs.dummy;

import com.gjgs.gjgs.modules.category.entity.Category;
import com.gjgs.gjgs.modules.matching.entity.Matching;
import com.gjgs.gjgs.modules.matching.repository.interfaces.MatchingRepository;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.zone.entity.Zone;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("dev")
public class MatchingDummy {

    private final MatchingRepository matchingRepository;

    public Matching createMatching(Member member, Zone zone, Category category,String dayType,String timeType,int preferMemberCount){
        Matching matching = Matching.builder()
                .zone(zone)
                .member(member)
                .category(category)
                .dayType(dayType)
                .timeType(timeType)
                .preferMemberCount(preferMemberCount)
                .build();
        matchingRepository.save(matching);
        return matching;
    }
}
