package com.gjgs.gjgs.modules.bulletin.repositories;

import com.gjgs.gjgs.modules.bulletin.dto.search.BulletinSearchCondition;
import com.gjgs.gjgs.modules.bulletin.dto.search.BulletinSearchResponse;
import com.gjgs.gjgs.modules.bulletin.dto.search.QBulletinSearchResponse;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.gjgs.gjgs.modules.bulletin.entity.QBulletin.bulletin;
import static com.gjgs.gjgs.modules.category.entity.QCategory.category;
import static com.gjgs.gjgs.modules.lecture.entity.QLecture.lecture;
import static com.gjgs.gjgs.modules.team.entity.QTeam.team;
import static com.gjgs.gjgs.modules.zone.entity.QZone.zone;

@Repository
@RequiredArgsConstructor
public class BulletinSearchQueryRepositoryImpl implements BulletinSearchQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public Page<BulletinSearchResponse> searchBulletin(Pageable pageable, BulletinSearchCondition condition) {
        QueryResults<BulletinSearchResponse> results = query
                .select(
                        new QBulletinSearchResponse(
                                bulletin.id.as("bulletinId"),
                                lecture.thumbnailImageFileUrl.as("bulletinImageUrl"),
                                zone.id.as("zoneId"),
                                category.id.as("categoryId"),
                                bulletin.title.as("bulletinTitle"),
                                bulletin.age.as("age"),
                                bulletin.timeType.as("time"),
                                team.currentMemberCount.as("nowMembers"),
                                team.maxPeople.as("maxMembers")))
                .from(bulletin)
                .innerJoin(bulletin.lecture, lecture)
                .innerJoin(lecture.zone, zone)
                .innerJoin(lecture.category, category)
                .innerJoin(bulletin.team, team)
                .where(
                        containKeyword(condition.getKeyword()),
                        containCategoryList(condition.getCategoryIdList()),
                        eqZoneId(condition.getZoneId()),
                        isRecruit())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<BulletinSearchResponse> content = results.getResults();
        long count = results.getTotal();
        return new PageImpl<>(content, pageable, count);
    }

    @Override
    public Page<BulletinSearchResponse> findLecturePickBulletins(Long lectureId, Pageable pageable) {

        QueryResults<BulletinSearchResponse> results = query
                .select(new QBulletinSearchResponse(
                        bulletin.id.as("bulletinId"),
                        lecture.thumbnailImageFileUrl.as("bulletinImageUrl"),
                        zone.id.as("zoneId"),
                        category.id.as("categoryId"),
                        bulletin.title.as("bulletinTitle"),
                        bulletin.age.as("age"),
                        bulletin.timeType.as("time"),
                        team.currentMemberCount.as("nowMembers"),
                        team.maxPeople.as("maxMembers")))
                .from(bulletin)
                .innerJoin(bulletin.lecture, lecture)
                .innerJoin(lecture.zone, zone)
                .innerJoin(lecture.category, category)
                .innerJoin(bulletin.team, team)
                .where(lecture.id.eq(lectureId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(bulletin.lastModifiedDate.desc())
                .fetchResults();

        List<BulletinSearchResponse> content = results.getResults();
        long count = results.getTotal();
        return new PageImpl<>(content, pageable, count);
    }

    private BooleanExpression isRecruit() {
        return bulletin.status.eq(true);
    }

    private BooleanExpression eqZoneId(Long zoneId) {
        return zoneId == null
                ? null
                : zone.id.eq(zoneId);
    }

    private BooleanExpression containCategoryList(List<Long> categoryIdList) {
        return categoryIdList == null || categoryIdList.isEmpty()
                ? null
                : category.id.in(categoryIdList);
    }

    private BooleanExpression containKeyword(String keyword) {
        return !StringUtils.hasText(keyword)
                ? bulletin.title.contains("").or(bulletin.description.contains(""))
                : bulletin.title.contains(keyword).or(bulletin.description.contains(keyword));
    }
}
