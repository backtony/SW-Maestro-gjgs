package com.gjgs.gjgs.modules.favorite.repository.interfaces;

import com.gjgs.gjgs.modules.favorite.dto.LectureMemberDto;

import java.util.List;
import java.util.Optional;

public interface LectureMemberQueryRepository {
    List<LectureMemberDto> findNotFinishedLectureByUsername(String username);

    Optional<Long> findIdByLectureIdAndUsername(Long lectureId, String username);

    List<Long> findFavoriteLectureIdListByMemberId(Long memberId);
}
