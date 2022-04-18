package com.gjgs.gjgs.modules.team.repositories.implement;

import com.gjgs.gjgs.modules.member.entity.QMember;
import com.gjgs.gjgs.modules.team.dtos.QTeamAppliersResponse_TeamApplierResponse;
import com.gjgs.gjgs.modules.team.dtos.TeamAppliersResponse;
import com.gjgs.gjgs.modules.team.entity.TeamApplier;
import com.gjgs.gjgs.modules.team.repositories.interfaces.TeamApplierQueryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashSet;
import java.util.List;

import static com.gjgs.gjgs.modules.member.entity.QMember.member;
import static com.gjgs.gjgs.modules.team.entity.QTeam.team;
import static com.gjgs.gjgs.modules.team.entity.QTeamApplier.teamApplier;

@Repository
@RequiredArgsConstructor
public class TeamApplierQueryRepositoryImpl implements TeamApplierQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public List<TeamApplier> findWithTeamLeaderByTeamId(Long teamId) {
        QMember leader = new QMember("leader");

        return query.select(teamApplier)
                    .from(teamApplier)
                    .leftJoin(teamApplier.applier, member).fetchJoin()
                    .join(teamApplier.appliedTeam, team).fetchJoin()
                    .join(team.leader, leader).fetchJoin()
                    .where(team.id.eq(teamId))
                    .fetch();
    }

    @Override
    public long deleteApplier(Long teamId, Long applierId) {
        return query.delete(teamApplier)
                .where(teamApplier.appliedTeam.id.eq(teamId),
                        teamApplier.applier.id.eq(applierId)).execute();
    }

    @Override
    public TeamAppliersResponse findAppliers(Long teamId) {
        QMember applier = new QMember("applier");

        List<TeamAppliersResponse.TeamApplierResponse> result = query.select(new QTeamAppliersResponse_TeamApplierResponse(
                applier.id, applier.imageFileUrl, applier.nickname,
                applier.sex.stringValue(), applier.age
        )).from(teamApplier)
                .join(teamApplier.applier, applier)
                .join(teamApplier.appliedTeam, team)
                .where(team.id.eq(teamId)).fetch();

        return TeamAppliersResponse.from(new LinkedHashSet<>(result));
    }

    @Override
    public List<Long> findApplierMemberIdList(Long teamId) {
        QMember applier = new QMember("applier");

        return query.select(applier.id)
                .from(teamApplier)
                .join(teamApplier.applier, applier)
                .join(teamApplier.appliedTeam, team)
                .where(team.id.eq(teamId))
                .fetch();
    }
}
