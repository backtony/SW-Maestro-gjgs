package com.gjgs.gjgs.modules.bulletin.repositories;

import com.gjgs.gjgs.modules.bulletin.dto.search.BulletinSearchCondition;
import com.gjgs.gjgs.modules.bulletin.dto.search.BulletinSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BulletinSearchQueryRepository {

    Page<BulletinSearchResponse> searchBulletin(Pageable pageable, BulletinSearchCondition condition);

    Page<BulletinSearchResponse> findLecturePickBulletins(Long lectureId, Pageable pageable);
}
