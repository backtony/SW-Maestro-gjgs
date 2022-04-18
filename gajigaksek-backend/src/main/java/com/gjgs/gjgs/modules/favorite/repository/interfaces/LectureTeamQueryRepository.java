package com.gjgs.gjgs.modules.favorite.repository.interfaces;

import com.gjgs.gjgs.modules.favorite.dto.LectureTeamDto;

import java.util.List;

public interface LectureTeamQueryRepository {
    List<LectureTeamDto> findNotFinishedLectureByTeamId(Long teamId);

    List<Long> findTeamByLectureIdAndTeamIdList(Long lectureId, List<Long> teamIdList);
}
