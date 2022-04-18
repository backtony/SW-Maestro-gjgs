package com.gjgs.gjgs.modules.utils.aop;

import com.gjgs.gjgs.modules.exception.member.MemberNotFoundException;
import com.gjgs.gjgs.modules.favorite.repository.interfaces.BulletinMemberQueryRepository;
import com.gjgs.gjgs.modules.favorite.repository.interfaces.LectureMemberQueryRepository;
import com.gjgs.gjgs.modules.lecture.dtos.FavoriteResponse;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberRepository;
import com.gjgs.gjgs.modules.team.dtos.AbstractSetLeaderFavoriteLecture;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Aspect
@Component
@Transactional
@RequiredArgsConstructor
public class MarkFavoriteAspect {

    private final SecurityUtil securityUtil;
    private final BulletinMemberQueryRepository bulletinMemberQueryRepository;
    private final MemberRepository memberRepository;
    private final LectureMemberQueryRepository lectureMemberQueryRepository;

    @AfterReturning(value = "@annotation(LoginMemberFavoriteBulletin)", returning = "bulletins")
    public void markFavoriteBulletin(Slice<? extends FavoriteResponse> bulletins) {
        if (securityUtil.getCurrentUsername().isPresent()) {
            String username = securityUtil.getCurrentUsername().get();
            List<Long> favoriteBulletinIdList = bulletinMemberQueryRepository.findFavoriteBulletinIdListByUsername(username);
            bulletins.getContent().forEach(response -> response.changeMyFavoriteContents(favoriteBulletinIdList));
        }
    }

    @AfterReturning(value = "@annotation(LoginMemberFavoriteLecture)", returning = "detailResponse")
    public void markFavoriteLecture(AbstractSetLeaderFavoriteLecture detailResponse) {
        if (securityUtil.getCurrentUsername().isPresent()) {
            Long memberId = findMemberId();
            List<Long> favoriteLectureIdList = findFavoriteLectureId(memberId);
            detailResponse.setLeaderMyFavoriteLecture(memberId, favoriteLectureIdList);
        }
    }

    @AfterReturning(value = "@annotation(LoginMemberFavoriteLecture)", returning = "lectures")
    public void markFavoriteLecture(Slice<? extends FavoriteResponse> lectures) {
        if (securityUtil.getCurrentUsername().isPresent()) {
            Long memberId = findMemberId();
            List<Long> favoriteLectureIdList = findFavoriteLectureId(memberId);
            lectures.getContent().forEach(response -> response.changeMyFavoriteContents(favoriteLectureIdList));
        }
    }

    @AfterReturning(value = "@annotation(LoginMemberFavoriteLecture)", returning = "detailResponse")
    public void markFavoriteLecture(FavoriteResponse detailResponse) {
        if (securityUtil.getCurrentUsername().isPresent()) {
            Long memberId = findMemberId();
            List<Long> favoriteLectureIdList = findFavoriteLectureId(memberId);
            detailResponse.changeMyFavoriteContents(favoriteLectureIdList);
        }
    }

    private List<Long> findFavoriteLectureId(Long memberId) {
        return lectureMemberQueryRepository.findFavoriteLectureIdListByMemberId(memberId);
    }

    private Long findMemberId() {
        return memberRepository.findIdByUsername(securityUtil.getCurrentUsername().get()).orElseThrow(() -> new MemberNotFoundException());
    }
}
