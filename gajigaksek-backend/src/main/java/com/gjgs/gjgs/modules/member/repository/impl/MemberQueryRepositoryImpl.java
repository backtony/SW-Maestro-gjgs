package com.gjgs.gjgs.modules.member.repository.impl;

import com.gjgs.gjgs.modules.matching.dto.MemberFcmIncludeNicknameDto;
import com.gjgs.gjgs.modules.matching.dto.QMemberFcmIncludeNicknameDto;
import com.gjgs.gjgs.modules.member.dto.mypage.*;
import com.gjgs.gjgs.modules.member.dto.search.MemberSearchCondition;
import com.gjgs.gjgs.modules.member.dto.search.MemberSearchResponse;
import com.gjgs.gjgs.modules.member.dto.search.QMemberSearchResponse;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.enums.Authority;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberQueryRepository;
import com.gjgs.gjgs.modules.notification.dto.MemberFcmDto;
import com.gjgs.gjgs.modules.notification.dto.QMemberFcmDto;
import com.gjgs.gjgs.modules.notification.enums.TargetType;
import com.gjgs.gjgs.modules.utils.querydsl.QueryDslUtil;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.gjgs.gjgs.modules.bulletin.entity.QBulletin.bulletin;
import static com.gjgs.gjgs.modules.category.entity.QCategory.category;
import static com.gjgs.gjgs.modules.lecture.entity.QLecture.lecture;
import static com.gjgs.gjgs.modules.member.entity.QMember.member;
import static com.gjgs.gjgs.modules.member.entity.QMemberCategory.memberCategory;
import static com.gjgs.gjgs.modules.member.entity.QMemberCoupon.memberCoupon;
import static com.gjgs.gjgs.modules.team.entity.QTeam.team;
import static com.gjgs.gjgs.modules.zone.entity.QZone.zone;
import static org.springframework.util.ObjectUtils.isEmpty;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepositoryImpl implements MemberQueryRepository {

    private final JPAQueryFactory query;

    public List<MyBulletinDto> findMyBulletinsByUsername(String username) {
        return query
                .select(new QMyBulletinDto(
                        bulletin.id.as("bulletinId"),
                        bulletin.lecture.thumbnailImageFileUrl,
                        bulletin.lecture.zone.id.as("zoneId"),
                        bulletin.title,
                        bulletin.age,
                        bulletin.timeType,
                        bulletin.team.currentMemberCount,
                        bulletin.team.maxPeople,
                        bulletin.status
                ))
                .from(bulletin)
                .join(bulletin.lecture, lecture)
                .join(bulletin.team, team)
                .join(team.leader, member)
                    .on(member.username.eq(username))
                .fetch()
                ;
    }

    @Override
    public Optional<Member> findWithZoneAndFavoriteCategoryAndCategoryById(Long memberId) {
        return Optional.ofNullable(
                query
                        .select(member)
                        .from(member)
                        .leftJoin(member.zone, zone).fetchJoin()
                        .leftJoin(member.memberCategories, memberCategory).fetchJoin()
                        .leftJoin(memberCategory.category, category).fetchJoin()
                        .where(member.id.eq(memberId))
                        .fetchOne()
        );
    }

    @Override
    public Optional<Member> findWithCouponByUsername(String username) {
        return Optional.ofNullable(
                query.select(member).distinct()
                        .from(member)
                        .leftJoin(member.coupons, memberCoupon).fetchJoin()
                        .where(usernameEq(username))
                        .fetchOne()
        );
    }

    @Override
    public Page<MemberSearchResponse> findPagingMemberByCondition(Pageable pageable, MemberSearchCondition cond) {
        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        QueryResults<MemberSearchResponse> results = query
                .select(new QMemberSearchResponse(
                        member.id,
                        member.nickname,
                        member.phone,
                        member.authority,
                        member.createdDate
                ))
                .from(member)
                .where(containNickname(cond.getNickname()),
                        authorityEq(cond.getAuthority()),
                        createdDateBetween(cond.getCreatedDateStart(),cond.getCreatedDateEnd()))
                .orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MemberSearchResponse> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content,pageable,total);
    }



    @Override
    public List<MemberFcmDto> findMemberFcmDtoByTargetTypeAndMemberIdListAndMemberEventAlarm(TargetType targetType, List<Long> memberIdList, boolean eventAlarm) {
        return query
                .select(new QMemberFcmDto(
                        member.id.as("memberId"),
                        member.fcmToken
                ))
                .from(member)
                .where(checkTargetType(memberIdList,targetType),member.eventAlarm.eq(eventAlarm))
                .fetch()
                ;
    }

    @Override
    public Optional<MemberFcmIncludeNicknameDto> findMemberFcmDtoByUsername(String username) {
        return Optional.ofNullable(
                query
                    .select(new QMemberFcmIncludeNicknameDto(
                            member.id.as("memberId"),
                            member.fcmToken,
                            member.nickname
                    ))
                    .from(member)
                    .where(usernameEq(username))
                    .fetchOne()
        );
    }

    @Override
    public Optional<TotalRewardDto> findTotalRewardDtoByUsername(String username) {
        return Optional.ofNullable(
                query
                    .select(new QTotalRewardDto(
                            member.id.as("memberId"),
                            member.totalReward
                    ))
                    .from(member)
                    .where(usernameEq(username))
                    .fetchOne()
        );
    }

    @Override
    public AlarmStatusResponse findAlarmStatusByUsername(String username) {
        return query
                .select(new QAlarmStatusResponse(
                        member.eventAlarm
                ))
                .from(member)
                .where(usernameEq(username))
                .fetchOne();
    }

    private BooleanExpression usernameEq(String username) {
        return username == null
                ? null
                : member.username.eq(username);
    }

    private BooleanExpression checkTargetType(List<Long> memberIdList,TargetType targetType) {
        return targetType.equals(TargetType.ALL)
                ? null
                : member.id.in(memberIdList);
    }


    private BooleanExpression containNickname(String nickname) {
        return StringUtils.hasText(nickname)
                ? member.nickname.contains(nickname)
                : null;
    }

    private BooleanExpression authorityEq(Authority authority) {
        return authority == null
                ? null
                : member.authority.eq(authority);
    }

    private BooleanExpression createdDateBetween(LocalDate start, LocalDate end) {
        // 둘 다 안들어왔다면
        if (start == null && end == null){
            return null;
        }
        // end만 들어왔다면
        if (start == null){
            return member.createdDate.loe(end.atTime(LocalTime.MAX));
        }
        // start만 들어왔다면
        if (end == null){
            return member.createdDate.goe(start.atStartOfDay());
        }

        return member.createdDate.between(start.atStartOfDay(),end.atTime(LocalTime.MAX));
    }

    private List<OrderSpecifier> getAllOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier> ORDERS = new ArrayList<>();

        if (!isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;


                switch (order.getProperty()) {
                    case "createdDate":
                        OrderSpecifier<?> createdDate = QueryDslUtil
                                .getSortedColumn(direction, member, "createdDate");
                        ORDERS.add(createdDate);
                        break;
                    default:
                        break;
                }
            }
        }

        return ORDERS;
    }
}
