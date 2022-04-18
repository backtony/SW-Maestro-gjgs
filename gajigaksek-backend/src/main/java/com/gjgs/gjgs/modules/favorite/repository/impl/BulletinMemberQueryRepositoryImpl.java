package com.gjgs.gjgs.modules.favorite.repository.impl;


import com.gjgs.gjgs.modules.favorite.dto.FavoriteBulletinDto;
import com.gjgs.gjgs.modules.favorite.dto.QFavoriteBulletinDto;
import com.gjgs.gjgs.modules.favorite.repository.interfaces.BulletinMemberQueryRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.gjgs.gjgs.modules.bulletin.entity.QBulletin.bulletin;
import static com.gjgs.gjgs.modules.favorite.entity.QBulletinMember.bulletinMember;
import static com.gjgs.gjgs.modules.lecture.entity.QLecture.lecture;
import static com.gjgs.gjgs.modules.member.entity.QMember.member;
import static com.gjgs.gjgs.modules.team.entity.QTeam.team;


@Repository
@RequiredArgsConstructor
public class BulletinMemberQueryRepositoryImpl implements BulletinMemberQueryRepository {

    private final JPAQueryFactory query;


    public List<FavoriteBulletinDto> findBulletinMemberDtoByUsername(String username) {

        return query
                .select(new QFavoriteBulletinDto(
                        bulletin.id.as("bulletinId"),
                        bulletinMember.id.as("bulletinMemberId"),
                        bulletin.lecture.thumbnailImageFileUrl,
                        bulletin.lecture.zone.id.as("zoneId"),
                        bulletin.title,
                        bulletin.age,
                        bulletin.timeType,
                        bulletin.team.currentMemberCount,
                        bulletin.team.maxPeople)).distinct()
                .from(bulletinMember)
                .join(bulletinMember.member, member).on(bulletinMember.member.username.eq(username))
                .join(bulletinMember.bulletin, bulletin)
                .join(bulletin.team, team)
                .join(bulletin.lecture, lecture)
                //.leftJoin(lecture.zone, zone)
                //.where(bulletinMember.member.username.eq(username))
                .where(bulletin.status.eq(true))
                .fetch();
    }

    @Override
    public Boolean existsByBulletinIdAndBulletinStatusAndMemberId(Long bulletinId, boolean bool, Long memberId) {
        Integer fetchOne = query
                .selectOne()
                .from(bulletinMember)
                .join(bulletinMember.bulletin, bulletin)
                .where(bulletinIdEq(bulletinId), bulletinMember.bulletin.status.eq(bool)
                        , bulletinMember.member.id.eq(memberId))
                .fetchFirst();

        return fetchOne != null;
    }

    @Override
    public Optional<Long> findIdByBulletinIdAndUsername(Long bulletinId, String username) {
        return Optional.ofNullable(
                query
                    .select(bulletinMember.id)
                    .from(bulletinMember)
                    .join(bulletinMember.member, member)
                    .where(bulletinIdEq(bulletinId),usernameEq(username))
                    .fetchOne()
        );
    }

    @Override
    public List<Long> findFavoriteBulletinIdListByUsername(String username) {
        return query.select(bulletin.id)
                .from(bulletinMember)
                .join(bulletinMember.bulletin, bulletin)
                .join(bulletinMember.member, member)
                .where(member.username.eq(username))
                .fetch();
    }

    private BooleanExpression bulletinIdEq(Long bulletinId) {
        return bulletinId == null
                ? null
                : bulletinMember.bulletin.id.eq(bulletinId);
    }

    private BooleanExpression usernameEq(String username) {
        return username == null
                ? null
                : bulletinMember.member.username.eq(username);
    }



}
