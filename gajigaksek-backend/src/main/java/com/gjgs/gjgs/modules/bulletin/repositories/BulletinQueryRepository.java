package com.gjgs.gjgs.modules.bulletin.repositories;

import com.gjgs.gjgs.modules.bulletin.dto.BulletinDetailResponse;
import com.gjgs.gjgs.modules.bulletin.entity.Bulletin;
import com.gjgs.gjgs.modules.team.dtos.MyLeadTeamsResponse;

import java.util.Optional;

public interface BulletinQueryRepository {

    Optional<Bulletin> findWithLecture(Long bulletinId);

    BulletinDetailResponse findBulletinDetail(Long bulletinId);

    long deleteFavoriteBulletinsById(Long bulletinId);

    Optional<Bulletin> findWithTeamByTeamId(Long teamId);

    Optional<Bulletin> findWithLectureTeam(Long bulletinId);

    MyLeadTeamsResponse findWithLectureByMemberId(MyLeadTeamsResponse response);

    Optional<Bulletin> findWithTeamLeaderByBulletinIdLeaderUsername(Long bulletinId, String username);

    Optional<Long> findIdByTeamId(Long teamId);
}
