package com.gjgs.gjgs.modules.favorite.repository.impl;


import com.gjgs.gjgs.modules.favorite.dto.LectureMemberDto;
import com.gjgs.gjgs.modules.favorite.dto.QLectureMemberDto;
import com.gjgs.gjgs.modules.favorite.repository.interfaces.LectureMemberQueryRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.gjgs.gjgs.modules.favorite.entity.QLectureMember.lectureMember;
import static com.gjgs.gjgs.modules.lecture.entity.QLecture.lecture;
import static com.gjgs.gjgs.modules.member.entity.QMember.member;
import static com.gjgs.gjgs.modules.zone.entity.QZone.zone;


@Repository
@RequiredArgsConstructor
public class LectureMemberQueryRepositoryImpl implements LectureMemberQueryRepository {

    private final JPAQueryFactory query;

    public List<LectureMemberDto> findNotFinishedLectureByUsername(String username) {
        // join을 하지 않으면 crossJoin 발생
        return query
                .select(new QLectureMemberDto(
                        lectureMember.id.as("lectureMemberId"),
                        lectureMember.lecture.id.as("lectureId"),
                        lectureMember.lecture.thumbnailImageFileUrl,
                        lectureMember.lecture.zone.id.as("zoneId"),
                        lectureMember.lecture.title,
                        lectureMember.lecture.price
                ))
                .from(lectureMember)
                .join(lectureMember.lecture, lecture)
                .join(lectureMember.lecture.zone, zone)
                .join(lectureMember.member, member)
                .where(usernameEq(username), lectureStatus(false))
                .fetch();
    }


    @Override
    public Optional<Long> findIdByLectureIdAndUsername(Long lectureId, String username) {


        return Optional.ofNullable(
                query
                        .select(lectureMember.id)
                        .from(lectureMember)
                        .join(lectureMember.member, member)
                        .join(lectureMember.lecture,lecture)
                        .where(lectureIdEq(lectureId), usernameEq(username))
                        .fetchOne());
    }

    @Override
    public List<Long> findFavoriteLectureIdListByMemberId(Long memberId) {
        return query.select(lecture.id)
                .from(lectureMember)
                .join(lectureMember.lecture, lecture)
                .join(lectureMember.member, member)
                .where(member.id.eq(memberId))
                .fetch();
    }


    private BooleanExpression usernameEq(String username) {
        return username == null
                ? null
                : lectureMember.member.username.eq(username);
    }

    private BooleanExpression lectureIdEq(Long lectureId) {
        return lectureId == null
                ? null
                : lectureMember.lecture.id.eq(lectureId);
    }

    private BooleanExpression lectureStatus(boolean bool) {
        return bool == true
                ? lectureMember.lecture.finished.eq(true)
                : lectureMember.lecture.finished.eq(false);
    }


}
