package com.gjgs.gjgs.modules.team.repositories.implement;

import com.gjgs.gjgs.modules.exception.team.TeamNotFoundException;
import com.gjgs.gjgs.modules.member.entity.QMember;
import com.gjgs.gjgs.modules.team.dtos.*;
import com.gjgs.gjgs.modules.team.entity.Team;
import com.gjgs.gjgs.modules.team.repositories.interfaces.TeamQueryRepository;
import com.gjgs.gjgs.modules.zone.entity.QZone;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.gjgs.gjgs.modules.category.entity.QCategory.category;
import static com.gjgs.gjgs.modules.favorite.entity.QLectureTeam.lectureTeam;
import static com.gjgs.gjgs.modules.lecture.entity.QLecture.lecture;
import static com.gjgs.gjgs.modules.member.entity.QMember.member;
import static com.gjgs.gjgs.modules.team.entity.QMemberTeam.memberTeam;
import static com.gjgs.gjgs.modules.team.entity.QTeam.team;
import static com.gjgs.gjgs.modules.team.entity.QTeamApplier.teamApplier;
import static com.gjgs.gjgs.modules.team.entity.QTeamCategory.teamCategory;
import static com.gjgs.gjgs.modules.zone.entity.QZone.zone;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.set;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Repository
@RequiredArgsConstructor
public class TeamQueryRepositoryImpl implements TeamQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public Optional<Team> findWithTeamMembers(Long teamId) {
        QMember leader = new QMember("leader");

        return Optional.ofNullable(
                query.selectFrom(team)
                        .leftJoin(team.teamMembers, memberTeam).fetchJoin()
                        .leftJoin(memberTeam.member, member).fetchJoin()
                        .join(team.leader, leader).fetchJoin()
                        .where(team.id.eq(teamId)).fetchOne());
    }

    @Override
    public Optional<Team> findWithLeaderZoneCategories(Long teamId) {
        QMember leader = new QMember("leader");

        return Optional.ofNullable(
                query.selectFrom(team).distinct()
                        .join(team.leader, leader).fetchJoin()
                        .join(team.zone, zone).fetchJoin()
                        .where(team.id.eq(teamId))
                        .fetchOne());
    }

    @Override
    public long deleteTeamCategories(Long teamId) {
        return query.delete(teamCategory)
                .where(teamCategory.team.id.eq(teamId))
                .execute();
    }

    @Override
    public List<Team> findMyAllTeamByUsername(String username) {
        QMember leader = new QMember("leader");

        return query
                .selectFrom(team).distinct()
                .join(team.leader, leader).fetchJoin()
                .leftJoin(team.teamMembers, memberTeam).fetchJoin()
                .leftJoin(memberTeam.member, member).fetchJoin()
                .where(leader.username.eq(username)
                        .or(member.username.eq(username)))
                .fetch();
    }

    @Override
    public Optional<Team> findWithLeader(Long teamId) {
        return Optional.ofNullable(
                query.selectFrom(team)
                        .join(team.leader, member).fetchJoin()
                        .where(team.id.eq(teamId)).fetchOne());
    }

    @Override
    public long deleteTeamLectures(Long teamId) {
        return query.delete(lectureTeam)
                .where(lectureTeam.team.id.eq(teamId))
                .execute();
    }

    @Override
    public long deleteTeamAppliers(Long teamId) {
        return query.delete(teamApplier)
                .where(teamApplier.appliedTeam.id.eq(teamId)).execute();
    }

    @Override
    public MyLeadTeamsResponse findMyTeamByUsername(String username) {
        List<Tuple> tuple = query.select(team.id, team.teamName)
                .from(team)
                .innerJoin(team.leader, member)
                .where(member.username.eq(username))
                .orderBy(team.createdDate.desc())
                .fetch();

        List<MyLeadTeamsResponse.MyLeadTeamsWithBulletin> list = tuple.stream()
                .map(t -> MyLeadTeamsResponse.MyLeadTeamsWithBulletin
                        .builder()
                        .teamId(t.get(team.id))
                        .teamName(t.get(team.teamName))
                        .build())
                .collect(toList());

        return MyLeadTeamsResponse.of(list);
    }

    @Override
    public TeamDetailResponse findTeamDetail(Long teamId) {
        QMember leader = new QMember("leader");
        QZone lectureZone = new QZone("lectureZone");

        Optional<TeamDetailResponse> result = Optional.of(query.from(team).distinct()
                .innerJoin(teamCategory).on(teamCategory.team.id.eq(team.id))
                .innerJoin(teamCategory.category, category)
                .innerJoin(team.zone, zone)
                .innerJoin(team.leader, leader)
                .leftJoin(team.teamMembers, memberTeam)
                .leftJoin(memberTeam.member, member)
                .leftJoin(lectureTeam).on(lectureTeam.team.id.eq(team.id))
                .leftJoin(lectureTeam.lecture, lecture)
                .leftJoin(lecture.zone, lectureZone)
                .where(team.id.eq(teamId))
                .transform(groupBy(team.id).as(new QTeamDetailResponse(
                        team.teamName.as("teamName"),
                        team.dayType.as("day"),
                        team.timeType.as("time"),
                        team.currentMemberCount.as("applyPeople"),
                        team.maxPeople.as("maxPeople"),
                        zone.id.as("zoneId"),
                        new QTeamDetailResponse_TeamMembers(
                                leader.id, leader.imageFileUrl, leader.nickname, leader.sex.stringValue(),
                                leader.age, leader.profileText
                        ),
                        set(category.id),
                        set(new QTeamDetailResponse_TeamMembers(
                                member.id, member.imageFileUrl, member.nickname, member.sex.stringValue(),
                                member.age, member.profileText
                        )),
                        set(new QTeamDetailResponse_FavoriteLecture(
                                lecture.id, lectureZone.id, lecture.title, lecture.price.regularPrice,
                                lecture.thumbnailImageFileUrl
                        ))))).get(teamId));

        TeamDetailResponse response = result
                .orElseThrow(() -> new TeamNotFoundException());
        response.clearEmptySet();
        return response;
    }

    @Override
    public MyTeamListResponse findMyTeamListByUsername(String username) {
        QMember leader = new QMember("leader");

        Map<Long, MyTeamListResponse.MyTeam> transform = query.from(team)
                .innerJoin(team.leader, leader)
                .innerJoin(teamCategory).on(teamCategory.team.id.eq(team.id))
                .innerJoin(teamCategory.category, category)
                .leftJoin(team.teamMembers, memberTeam)
                .leftJoin(memberTeam.member, member)
                .where(leader.username.eq(username).or(member.username.eq(username)))
                .transform(groupBy(team.id).as(
                        new QMyTeamListResponse_MyTeam(
                                team.id,
                                team.teamName,
                                team.currentMemberCount,
                                team.maxPeople,
                                set(category.id),
                                leader.username.eq(username)
                        )));

        return MyTeamListResponse.of(
                transform.keySet().stream().map(transform::get).collect(toSet())
        );
    }

    @Override
    public long deleteTeamMember(Long teamId, Long memberId) {
        return query.delete(memberTeam)
                .where(memberTeam.team.id.eq(teamId)
                        .and(memberTeam.member.id.eq(memberId))).execute();
    }

    @Override
    public long deleteTeamMembers(Long teamId) {
        return query.delete(memberTeam)
                .where(memberTeam.team.id.eq(teamId)).execute();
    }

    @Override
    public Boolean existByIdLeaderUsername(Long teamId, String leaderUsername) {
        Integer result = query.selectOne().from(team)
                .join(team.leader, member)
                .where(team.id.eq(teamId),
                        member.username.eq(leaderUsername))
                .fetchFirst();

        return result != null;
    }
}
