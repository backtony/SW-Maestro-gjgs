package com.gjgs.gjgs.modules.member.repository.interfaces;

import com.gjgs.gjgs.modules.matching.dto.MemberFcmIncludeNicknameDto;
import com.gjgs.gjgs.modules.member.dto.mypage.AlarmStatusResponse;
import com.gjgs.gjgs.modules.member.dto.mypage.MyBulletinDto;
import com.gjgs.gjgs.modules.member.dto.mypage.TotalRewardDto;
import com.gjgs.gjgs.modules.member.dto.search.MemberSearchCondition;
import com.gjgs.gjgs.modules.member.dto.search.MemberSearchResponse;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.notification.dto.MemberFcmDto;
import com.gjgs.gjgs.modules.notification.enums.TargetType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MemberQueryRepository {

    List<MyBulletinDto> findMyBulletinsByUsername(String username);

    Optional<Member> findWithZoneAndFavoriteCategoryAndCategoryById(Long memberId);

    Optional<Member> findWithCouponByUsername(String username);

    Page<MemberSearchResponse> findPagingMemberByCondition(Pageable pageable, MemberSearchCondition cond);

    List<MemberFcmDto> findMemberFcmDtoByTargetTypeAndMemberIdListAndMemberEventAlarm(TargetType targetType, List<Long> memberIdList, boolean eventAlarm);

    Optional<MemberFcmIncludeNicknameDto> findMemberFcmDtoByUsername(String username);

    Optional<TotalRewardDto> findTotalRewardDtoByUsername(String username);

    AlarmStatusResponse findAlarmStatusByUsername(String username);
}
