package com.gjgs.gjgs.modules.favorite.repository.interfaces;

import com.gjgs.gjgs.modules.favorite.dto.FavoriteBulletinDto;

import java.util.List;
import java.util.Optional;

public interface BulletinMemberQueryRepository {

    List<FavoriteBulletinDto> findBulletinMemberDtoByUsername(String username);

    Boolean existsByBulletinIdAndBulletinStatusAndMemberId(Long bulletinId, boolean bool, Long memberId);

    Optional<Long> findIdByBulletinIdAndUsername(Long bulletinId, String username);

    List<Long> findFavoriteBulletinIdListByUsername(String username);
}
