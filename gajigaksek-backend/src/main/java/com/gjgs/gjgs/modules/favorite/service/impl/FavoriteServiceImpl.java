package com.gjgs.gjgs.modules.favorite.service.impl;

import com.gjgs.gjgs.modules.bulletin.entity.Bulletin;
import com.gjgs.gjgs.modules.exception.favorite.BulletinMemberNotFoundException;
import com.gjgs.gjgs.modules.exception.favorite.LectureMemberNotExistException;
import com.gjgs.gjgs.modules.exception.favorite.LectureTeamNotExistException;
import com.gjgs.gjgs.modules.exception.lecture.LectureNotFoundException;
import com.gjgs.gjgs.modules.exception.member.MemberNotFoundException;
import com.gjgs.gjgs.modules.exception.team.NotMemberOfTeamException;
import com.gjgs.gjgs.modules.exception.team.TeamNotFoundException;
import com.gjgs.gjgs.modules.favorite.dto.FavoriteBulletinDto;
import com.gjgs.gjgs.modules.favorite.dto.LectureMemberDto;
import com.gjgs.gjgs.modules.favorite.dto.LectureTeamDto;
import com.gjgs.gjgs.modules.favorite.dto.MyTeamAndIsIncludeFavoriteLectureDto;
import com.gjgs.gjgs.modules.favorite.entity.BulletinMember;
import com.gjgs.gjgs.modules.favorite.entity.LectureMember;
import com.gjgs.gjgs.modules.favorite.entity.LectureTeam;
import com.gjgs.gjgs.modules.favorite.repository.interfaces.*;
import com.gjgs.gjgs.modules.favorite.service.interfaces.FavoriteService;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.lecture.enums.LectureStatus;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureRepository;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberRepository;
import com.gjgs.gjgs.modules.team.entity.Team;
import com.gjgs.gjgs.modules.team.repositories.interfaces.TeamQueryRepository;
import com.gjgs.gjgs.modules.team.repositories.interfaces.TeamRepository;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final SecurityUtil securityUtil;
    private final MemberRepository memberRepository;
    private final LectureMemberRepository lectureMemberRepository;
    private final LectureMemberQueryRepository lectureMemberQueryRepository;
    private final TeamRepository teamRepository;
    private final BulletinMemberQueryRepository bulletinMemberQueryRepository;
    private final LectureTeamQueryRepository lectureTeamQueryRepository;
    private final BulletinMemberRepository bulletinMemberRepository;
    private final LectureRepository lectureRepository;
    private final LectureTeamRepository lectureTeamRepository;
    private final TeamQueryRepository teamQueryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<LectureMemberDto> getMyFavoriteLectures() {
        return lectureMemberQueryRepository.findNotFinishedLectureByUsername(getUsernameOrThrow());
    }

    @Override
    public void deleteMyFavoriteLecture(Long lectureId) {
        Long lectureMemberId = lectureMemberQueryRepository.findIdByLectureIdAndUsername(lectureId, getUsernameOrThrow())
                .orElseThrow(() -> new LectureMemberNotExistException());
        lectureMemberRepository.deleteById(lectureMemberId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LectureTeamDto> getTeamFavoriteLectures(Long teamId) {
        checkIsMemberOfTeam(teamId);
        return lectureTeamQueryRepository.findNotFinishedLectureByTeamId(teamId);
    }


    @Override
    public void deleteTeamFavoriteLecture(Long teamId, Long lectureId) {
        checkIsMemberOfTeam(teamId);
        Long lectureTeamId = lectureTeamRepository.findIdByLectureIdAndTeamId(lectureId, teamId)
                .orElseThrow(() -> new LectureTeamNotExistException());
        lectureTeamRepository.deleteById(lectureTeamId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FavoriteBulletinDto> getMyFavoriteBulletins() {
        return bulletinMemberQueryRepository.findBulletinMemberDtoByUsername(getUsernameOrThrow());
    }

    @Override
    public void deleteMyFavoriteBulletin(Long bulletinId) {
        Long bulletinMemberId = bulletinMemberQueryRepository.findIdByBulletinIdAndUsername(bulletinId, getUsernameOrThrow())
                .orElseThrow(() -> new BulletinMemberNotFoundException());
        bulletinMemberRepository.deleteById(bulletinMemberId);
    }

    @Override
    public void saveMyFavoriteLecture(Long lectureId) {
        CheckIsLectureStatusAccept(lectureId);
        Long memberId = memberRepository.findIdByUsername(getUsernameOrThrow())
                .orElseThrow(() -> new MemberNotFoundException());

        if (lectureMemberRepository.findByMemberIdAndLectureId(memberId, lectureId).isEmpty()) {
            LectureMember lectureMember = LectureMember.of(Lecture.of(lectureId), Member.from(memberId));
            lectureMemberRepository.save(lectureMember);
        }
    }

    @Override
    public void saveTeamFavoriteLecture(Long teamId, Long lectureId) {
        CheckIsLectureStatusAccept(lectureId);
        checkIsMemberOfTeam(teamId);

        if (lectureTeamRepository.findIdByLectureIdAndTeamId(lectureId, teamId).isEmpty()) {
            lectureTeamRepository.save(LectureTeam.of(Lecture.of(lectureId), Team.of(teamId)));
        }

    }

    private void CheckIsLectureStatusAccept(Long lectureId) {
        if (!lectureRepository.existsLectureByIdAndLectureStatus(lectureId, LectureStatus.ACCEPT)) {
            throw new LectureNotFoundException();
        }
    }

    private void checkIsMemberOfTeam(Long teamId) {
        Team teamWithMemberAndLeader = teamRepository.findWithMembersAndLeaderById(teamId)
                .orElseThrow(() -> new TeamNotFoundException());
        List<String> teamMemberUsernameList
                = teamWithMemberAndLeader.getTeamMembers().stream().map(team -> team.getMember().getUsername()).collect(Collectors.toList());
        String username = getUsernameOrThrow();

        if (isNotMemberOfTeam(teamWithMemberAndLeader, teamMemberUsernameList, username)) {
            throw new NotMemberOfTeamException();
        }
    }

    private boolean isNotMemberOfTeam(Team teamWithMemberAndLeader, List<String> teamMemberUsernameList, String username) {
        return (!teamMemberUsernameList.contains(username)) && (!teamWithMemberAndLeader.getLeader().getUsername().equals(username));
    }

    @Override
    public void saveMyFavoriteBulletin(Long bulletinId) {
        Long memberId = memberRepository.findIdByUsername(getUsernameOrThrow())
                .orElseThrow(() -> new MemberNotFoundException());

        if (!bulletinMemberQueryRepository.existsByBulletinIdAndBulletinStatusAndMemberId(bulletinId, true, memberId)) {
            bulletinMemberRepository.save(BulletinMember.of(Bulletin.of(bulletinId), Member.from(memberId)));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<MyTeamAndIsIncludeFavoriteLectureDto> getMyTeamAndIsIncludeFavoriteLecture(Long lectureId) {
        List<Team> myTeamList = getTeamList();
        List<Long> myTeamIdListOnlyIncludeLecture
                = lectureTeamQueryRepository.findTeamByLectureIdAndTeamIdList(
                lectureId,
                myTeamList.stream().map(team -> team.getId()).collect(Collectors.toList()));

        return createTeamListToMyTeamAndIsIncludeFavoriteLectureDtoList(myTeamList, myTeamIdListOnlyIncludeLecture);
    }

    private List<Team> getTeamList() {
        List<Team> myTeamList = teamQueryRepository.findMyAllTeamByUsername(getUsernameOrThrow());
        return myTeamList;
    }

    private List<MyTeamAndIsIncludeFavoriteLectureDto> createTeamListToMyTeamAndIsIncludeFavoriteLectureDtoList
            (List<Team> myTeamList, List<Long> myTeamIdListOnlyIncludeLecture) {

        return myTeamList.stream().map(team ->
                myTeamIdListOnlyIncludeLecture.contains(team.getId())
                        ? MyTeamAndIsIncludeFavoriteLectureDto.of(team, true)
                        : MyTeamAndIsIncludeFavoriteLectureDto.of(team, false)
        ).collect(Collectors.toList());
    }

    private String getUsernameOrThrow() {
        return securityUtil.getCurrentUsername()
                .orElseThrow(() -> new MemberNotFoundException());
    }

}
