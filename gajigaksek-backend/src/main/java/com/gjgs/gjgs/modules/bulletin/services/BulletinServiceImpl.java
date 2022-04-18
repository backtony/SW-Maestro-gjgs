package com.gjgs.gjgs.modules.bulletin.services;

import com.gjgs.gjgs.modules.bulletin.dto.BulletinChangeRecruitResponse;
import com.gjgs.gjgs.modules.bulletin.dto.BulletinDetailResponse;
import com.gjgs.gjgs.modules.bulletin.dto.BulletinIdResponse;
import com.gjgs.gjgs.modules.bulletin.dto.CreateBulletinRequest;
import com.gjgs.gjgs.modules.bulletin.dto.search.BulletinSearchCondition;
import com.gjgs.gjgs.modules.bulletin.dto.search.BulletinSearchResponse;
import com.gjgs.gjgs.modules.bulletin.entity.Bulletin;
import com.gjgs.gjgs.modules.bulletin.repositories.BulletinRepository;
import com.gjgs.gjgs.modules.exception.bulletin.BulletinNotFoundException;
import com.gjgs.gjgs.modules.exception.bulletin.BulletinNotFoundOrNotLeaderException;
import com.gjgs.gjgs.modules.exception.lecture.LectureNotFoundException;
import com.gjgs.gjgs.modules.exception.member.MemberNotFoundException;
import com.gjgs.gjgs.modules.exception.team.TeamNotFoundException;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureRepository;
import com.gjgs.gjgs.modules.team.entity.Team;
import com.gjgs.gjgs.modules.team.repositories.interfaces.TeamRepository;
import com.gjgs.gjgs.modules.utils.aop.LoginMemberFavoriteBulletin;
import com.gjgs.gjgs.modules.utils.aop.LoginMemberFavoriteLecture;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.gjgs.gjgs.modules.lecture.enums.LectureStatus.ACCEPT;

@Service
@Transactional
@RequiredArgsConstructor
public class BulletinServiceImpl implements BulletinService {

    private final TeamRepository teamRepository;
    private final LectureRepository lectureRepository;
    private final BulletinRepository bulletinRepository;
    private final SecurityUtil securityUtil;

    @Override
    public BulletinIdResponse createBulletin(CreateBulletinRequest request) {
        Optional<Bulletin> teamHasBulletin = bulletinRepository.findWithTeamByTeamId(request.getTeamId());
        if (teamHasBulletin.isPresent()) {
            return modifyBulletin(teamHasBulletin.get().getId(), request);
        }

        Bulletin bulletin = Bulletin.of(request);
        Long lectureId = relateLecture(request.getLectureId(), bulletin);
        Long teamId = relateTeam(request.getTeamId(), bulletin);

        Bulletin savedBulletin = bulletinRepository.save(bulletin);
        return BulletinIdResponse.of(savedBulletin.getId(), teamId, lectureId);
    }

    private Long relateTeam(Long teamId, Bulletin bulletin) {
        Team relateTeam = teamRepository.findById(teamId).orElseThrow(() -> new TeamNotFoundException());
        relateTeam.checkTeamIsFullDoThrow();
        bulletin.setTeam(relateTeam);
        return relateTeam.getId();
    }

    @Override
    public BulletinIdResponse deleteBulletin(Long bulletinId) {
        Bulletin deleteBulletin = bulletinRepository.findById(bulletinId).orElseThrow(() -> new BulletinNotFoundException());

        deleteBulletin.stopRecruit();
        return BulletinIdResponse.of(bulletinId);
    }

    @Override
    public BulletinIdResponse modifyBulletin(Long bulletinId, CreateBulletinRequest request) {
        Bulletin findBulletin = bulletinRepository.findWithLectureTeam(bulletinId)
                .orElseThrow(() -> new BulletinNotFoundException());

        Long modifiedLectureId = modifyLecture(findBulletin, request);
        findBulletin.modify(request);
        return BulletinIdResponse.of(findBulletin.getId(), modifiedLectureId);
    }

    private Long modifyLecture(Bulletin findBulletin, CreateBulletinRequest request) {
        if (findBulletin.isDifferenceLecture(request.getLectureId())) {
            return relateLecture(request.getLectureId(), findBulletin);
        }
        return findBulletin.getLectureId();
    }

    private Long relateLecture(Long lectureId, Bulletin bulletin) {
        if (!lectureRepository.existsLectureByIdAndLectureStatus(lectureId, ACCEPT)) {
            throw new LectureNotFoundException();
        }

        Lecture lecture = Lecture.of(lectureId);
        bulletin.setLecture(lecture);
        return lecture.getId();
    }

    @LoginMemberFavoriteBulletin
    @Override
    @Transactional(readOnly = true)
    public Page<BulletinSearchResponse> getBulletins(Pageable pageable, BulletinSearchCondition condition) {
        return bulletinRepository.searchBulletin(pageable, condition);
    }

    @LoginMemberFavoriteLecture
    @Override
    @Transactional(readOnly = true)
    public BulletinDetailResponse getBulletinDetails(Long bulletinId) {
        return bulletinRepository.findBulletinDetail(bulletinId);
    }

    @Override
    public BulletinChangeRecruitResponse changeRecruitStatus(Long bulletinId) {
        String username = securityUtil.getCurrentUsername().orElseThrow(() -> new MemberNotFoundException());
        Bulletin bulletin = bulletinRepository.findWithTeamLeaderByBulletinIdLeaderUsername(bulletinId, username)
                .orElseThrow(() -> new BulletinNotFoundOrNotLeaderException());
        bulletin.changeRecruitStatus();
        return BulletinChangeRecruitResponse.of(bulletin.getId(), bulletin.isStatus());
    }
}
