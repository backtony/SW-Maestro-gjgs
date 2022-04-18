package com.gjgs.gjgs.modules.matching.repository.impl;

import com.gjgs.gjgs.modules.matching.dto.MatchingRequest;
import com.gjgs.gjgs.modules.matching.dto.MemberFcmIncludeNicknameDto;
import com.gjgs.gjgs.modules.matching.dto.QMemberFcmIncludeNicknameDto;
import com.gjgs.gjgs.modules.matching.repository.interfaces.MatchingQueryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.gjgs.gjgs.modules.matching.entity.QMatching.matching;
import static com.gjgs.gjgs.modules.member.entity.QMember.member;


@Repository
@RequiredArgsConstructor
public class MatchingQueryRepositoryImpl implements MatchingQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public boolean existsByUsername(String username) {
        Integer fetchOne = query
                .selectOne()
                .from(matching)
                .join(matching.member, member)
                .where(matching.member.username.eq(username))
                .fetchFirst();
        return fetchOne != null;

    }

    @Override
    public List<MemberFcmIncludeNicknameDto> findMemberFcmDtoByMatchForm(MatchingRequest matchingRequest) {
        return query
                .select(new QMemberFcmIncludeNicknameDto(
                        matching.member.id.as("memberId"),
                        matching.member.fcmToken,
                        matching.member.username
                ))
                .from(matching)
                .join(matching.member, member)
                .where(matching.zone.id.eq(matchingRequest.getZoneId()),
                        matching.preferMemberCount.eq(matchingRequest.getPreferMemberCount()),
                        matching.timeType.eq(matchingRequest.getTimeType()),
                        matching.category.id.eq(matchingRequest.getCategoryId()),
                        matching.dayType.contains(matchingRequest.getDayType()))
                .limit(matchingRequest.getPreferMemberCount() - 1)
                .fetch();
    }


}
