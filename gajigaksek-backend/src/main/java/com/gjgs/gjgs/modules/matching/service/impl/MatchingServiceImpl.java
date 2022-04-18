package com.gjgs.gjgs.modules.matching.service.impl;

import com.gjgs.gjgs.modules.category.entity.Category;
import com.gjgs.gjgs.modules.exception.member.MemberNotFoundException;
import com.gjgs.gjgs.modules.matching.aop.CheckIsAlreadyMatching;
import com.gjgs.gjgs.modules.matching.dto.MatchingRequest;
import com.gjgs.gjgs.modules.matching.dto.MatchingStatusResponse;
import com.gjgs.gjgs.modules.matching.dto.MemberFcmIncludeNicknameDto;
import com.gjgs.gjgs.modules.matching.entity.Matching;
import com.gjgs.gjgs.modules.matching.enums.Status;
import com.gjgs.gjgs.modules.matching.event.MatchingCompleteEvent;
import com.gjgs.gjgs.modules.matching.repository.interfaces.MatchingQueryRepository;
import com.gjgs.gjgs.modules.matching.repository.interfaces.MatchingRepository;
import com.gjgs.gjgs.modules.matching.service.interfaces.MatchingService;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberQueryRepository;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberRepository;
import com.gjgs.gjgs.modules.team.entity.Team;
import com.gjgs.gjgs.modules.team.entity.TeamCategory;
import com.gjgs.gjgs.modules.team.repositories.interfaces.TeamCategoryRepository;
import com.gjgs.gjgs.modules.team.repositories.interfaces.TeamJdbcRepository;
import com.gjgs.gjgs.modules.team.repositories.interfaces.TeamRepository;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MatchingServiceImpl implements MatchingService {

    private final MatchingRepository matchingRepository;
    private final MatchingQueryRepository matchingQueryRepository;
    private final TeamRepository teamRepository;
    private final TeamJdbcRepository teamJdbcRepository;
    private final TeamCategoryRepository teamCategoryRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final SecurityUtil securityUtil;
    private final MemberQueryRepository memberQueryRepository;
    private final MemberRepository memberRepository;

    @Override
    @CheckIsAlreadyMatching
    public void matching(MatchingRequest matchingRequest) {
        MemberFcmIncludeNicknameDto memberFcmIncludeNicknameDto = memberQueryRepository.findMemberFcmDtoByUsername(getUsernameOrThrow())
                .orElseThrow(() -> new MemberNotFoundException());
        Member currentUser = Member.from(memberFcmIncludeNicknameDto);
        List<Member> matchingMemberList = matchingQueryRepository.findMemberFcmDtoByMatchForm(matchingRequest).stream()
                .map(Member::from).collect(Collectors.toList());

        if (canMatch(matchingRequest, matchingMemberList)) {
            List<Long> memberIdList = matchingMemberList.stream().map(mml -> mml.getId()).collect(Collectors.toList());
            Long teamId = saveTeam(matchingRequest, currentUser, matchingMemberList, memberIdList);
            matchingRepository.deleteAllWithBulkById(memberIdList);
            sendNotificationToMatchingMember(currentUser, matchingMemberList, teamId);
        } else {
            matchingRepository.save(Matching.of(matchingRequest, currentUser));
        }


    }

    private boolean canMatch(MatchingRequest matchingRequest, List<Member> matchingMemberList) {
        return matchingMemberList.size() == matchingRequest.getPreferMemberCount() - 1;
    }

    private Long saveTeam(MatchingRequest matchingRequest, Member currentUser, List<Member> matchingMemberList, List<Long> memberIdList) {
        Team team = teamRepository.save(Team.createMatchTeam(matchingRequest, currentUser, matchingMemberList));
        teamCategoryRepository.save(TeamCategory.of(team, Category.from(matchingRequest.getCategoryId())));
        teamJdbcRepository.insertMemberTeamList(team.getId(), memberIdList);
        return team.getId();
    }

    private void sendNotificationToMatchingMember(Member currentUser, List<Member> matchingMemberList, Long teamId) {
        matchingMemberList.add(currentUser);
        eventPublisher.publishEvent(
                new MatchingCompleteEvent(matchingMemberList, teamId));
    }


    @Override
    public void cancel() {
        Long currentUserId = memberRepository.findIdByUsername(getUsernameOrThrow())
                .orElseThrow(() -> new MemberNotFoundException());
        matchingRepository.deleteMatchingByMemberId(currentUserId);
    }

    @Override
    public MatchingStatusResponse status() {
        if (matchingQueryRepository.existsByUsername(getUsernameOrThrow())) {
            return MatchingStatusResponse.from(Status.MATCHING);
        }
        return MatchingStatusResponse.from(Status.NOT_MATCHING);
    }

    private String getUsernameOrThrow() {
        return securityUtil.getCurrentUsername()
                .orElseThrow(() -> new MemberNotFoundException());
    }


}
