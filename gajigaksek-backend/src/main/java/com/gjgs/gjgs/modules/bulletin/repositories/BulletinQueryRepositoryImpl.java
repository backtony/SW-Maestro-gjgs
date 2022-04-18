package com.gjgs.gjgs.modules.bulletin.repositories;

import com.gjgs.gjgs.modules.bulletin.dto.*;
import com.gjgs.gjgs.modules.bulletin.entity.Bulletin;
import com.gjgs.gjgs.modules.member.entity.QMember;
import com.gjgs.gjgs.modules.team.dtos.MyLeadTeamsResponse;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.gjgs.gjgs.modules.bulletin.entity.QBulletin.bulletin;
import static com.gjgs.gjgs.modules.category.entity.QCategory.category;
import static com.gjgs.gjgs.modules.favorite.entity.QBulletinMember.bulletinMember;
import static com.gjgs.gjgs.modules.lecture.entity.QLecture.lecture;
import static com.gjgs.gjgs.modules.member.entity.QMember.member;
import static com.gjgs.gjgs.modules.team.entity.QMemberTeam.memberTeam;
import static com.gjgs.gjgs.modules.team.entity.QTeam.team;
import static com.gjgs.gjgs.modules.zone.entity.QZone.zone;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static java.util.stream.Collectors.toList;

@Repository
@RequiredArgsConstructor
public class BulletinQueryRepositoryImpl implements BulletinQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public Optional<Bulletin> findWithLecture(Long bulletinId) {
        return Optional.ofNullable(
                query.select(bulletin)
                        .from(bulletin)
                        .join(bulletin.lecture, lecture).fetchJoin()
                        .where(bulletin.id.eq(bulletinId))
                        .fetchOne()
        );
    }

    @Override
    public BulletinDetailResponse findBulletinDetail(Long bulletinId) {
        QMember leader = new QMember("leader");
        QMember teamMember = new QMember("teamMember");

        return query.from(bulletin)
                .join(bulletin.team, team)
                .leftJoin(team.teamMembers, memberTeam)
                .leftJoin(memberTeam.member, teamMember)
                .join(team.leader, leader)
                .join(bulletin.lecture, lecture)
                .join(lecture.zone, zone)
                .join(lecture.category, category)
                .where(bulletin.id.eq(bulletinId))
                .transform(groupBy(bulletin.id).as(new QBulletinDetailResponse(
                        bulletin.id, bulletin.title, bulletin.dayType, bulletin.timeType,
                        bulletin.description, bulletin.age.stringValue(),
                        new QBulletinDetailResponse_BulletinsLecture(
                                lecture.id, zone.id, category.id, lecture.thumbnailImageFileUrl,
                                lecture.title, lecture.price.priceOne, lecture.price.priceTwo,
                                lecture.price.priceThree, lecture.price.priceFour),
                        new QBulletinDetailResponse_BulletinsTeam(
                                team.id, team.currentMemberCount, team.maxPeople,
                                new QBulletinDetailResponse_TeamMemberResponse(
                                        leader.id, leader.imageFileUrl, leader.nickname,
                                        leader.sex.stringValue(), leader.age, leader.profileText
                                ),
                                list(new QBulletinDetailResponse_TeamMemberResponse(
                                        teamMember.id, teamMember.imageFileUrl, teamMember.nickname,
                                        teamMember.sex.stringValue(), teamMember.age, teamMember.profileText
                                ))
                        )
                ))).get(bulletinId);
    }

    @Override
    public long deleteFavoriteBulletinsById(Long bulletinId) {
        return query.delete(bulletinMember)
                .where(bulletinMember.bulletin.id.eq(bulletinId)).execute();
    }

    @Override
    public Optional<Bulletin> findWithTeamByTeamId(Long teamId) {
        return Optional.ofNullable(
                query.selectFrom(bulletin)
                        .join(bulletin.team, team).fetchJoin()
                        .where(team.id.eq(teamId))
                        .fetchOne());
    }

    @Override
    public Optional<Bulletin> findWithLectureTeam(Long bulletinId) {
        return Optional.ofNullable(
                query.selectFrom(bulletin)
                        .join(bulletin.team, team).fetchJoin()
                        .join(bulletin.lecture, lecture).fetchJoin()
                        .where(bulletin.id.eq(bulletinId)).fetchOne());
    }

    @Override
    public MyLeadTeamsResponse findWithLectureByMemberId(MyLeadTeamsResponse response) {
        List<Long> myTeamsId = response.getMyLeadTeams().stream()
                .map(MyLeadTeamsResponse.MyLeadTeamsWithBulletin::getTeamId)
                .collect(toList());

        List<Tuple> bulletinTupleList = query.select(
                bulletin.id,
                bulletin.title,
                bulletin.age,
                bulletin.timeType,
                bulletin.description,
                bulletin.dayType,
                bulletin.status,
                team.id,
                lecture.id,
                lecture.price.priceOne,
                lecture.price.priceTwo,
                lecture.price.priceThree,
                lecture.price.priceFour,
                lecture.title,
                lecture.thumbnailImageFileUrl,
                zone.id,
                category.id)
                .from(bulletin)
                .innerJoin(bulletin.lecture, lecture)
                .leftJoin(bulletin.team, team)
                .innerJoin(lecture.category, category)
                .innerJoin(lecture.zone, zone)
                .where(team.id.in(myTeamsId)).fetch();

        setMyLeadTeamsResponse(response.getMyLeadTeams(), bulletinTupleList);
        return response;
    }

    @Override
    public Optional<Bulletin> findWithTeamLeaderByBulletinIdLeaderUsername(Long bulletinId, String username) {
        return Optional.ofNullable(
                query.selectFrom(bulletin)
                        .innerJoin(bulletin.team, team).fetchJoin()
                        .innerJoin(team.leader, member).fetchJoin()
                        .where(bulletin.id.eq(bulletinId),
                                member.username.eq(username)).fetchOne()
        );
    }

    @Override
    public Optional<Long> findIdByTeamId(Long teamId) {
        return Optional.ofNullable(
                query.select(bulletin.id)
                        .from(bulletin)
                        .join(bulletin.team, team)
                        .where(team.id.eq(teamId))
                        .fetchOne()
        );
    }

    private void setMyLeadTeamsResponse(List<MyLeadTeamsResponse.MyLeadTeamsWithBulletin> myLeadTeams,
                                        List<Tuple> bulletinTupleList) {
        myLeadTeams.forEach(myLeadTeam -> {
            bulletinTupleList.forEach(tuple -> {
                if (myLeadTeam.getTeamId().equals(tuple.get(team.id))) {
                    myLeadTeam.setBulletinLectureData(
                            tuple.get(bulletin.id),
                            tuple.get(bulletin.title),
                            tuple.get(bulletin.age).name(),
                            tuple.get(bulletin.timeType),
                            tuple.get(bulletin.description),
                            tuple.get(bulletin.dayType),
                            tuple.get(bulletin.status),
                            tuple.get(lecture.id),
                            tuple.get(lecture.price.priceOne),
                            tuple.get(lecture.price.priceTwo),
                            tuple.get(lecture.price.priceThree),
                            tuple.get(lecture.price.priceFour),
                            tuple.get(lecture.title),
                            tuple.get(lecture.thumbnailImageFileUrl),
                            tuple.get(zone.id),
                            tuple.get(category.id)
                    );
                }
            });
        });
    }
}
