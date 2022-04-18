package com.gjgs.gjgs.modules.team.repositories.implement;

import com.gjgs.gjgs.modules.team.repositories.interfaces.MemberTeamQueryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.gjgs.gjgs.modules.member.entity.QMember.member;
import static com.gjgs.gjgs.modules.team.entity.QMemberTeam.memberTeam;
import static com.gjgs.gjgs.modules.team.entity.QTeam.team;

@Repository
@RequiredArgsConstructor
public class MemberTeamQueryRepositoryImpl implements MemberTeamQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public Boolean existByApplierInTeamMember(Long teamId, Long memberId) {
        Integer result = query.selectOne().from(memberTeam)
                .join(memberTeam.team, team)
                .join(memberTeam.member, member)
                .where(member.id.eq(memberId),
                        team.id.eq(teamId))
                .fetchFirst();

        return result != null;
    }
}
