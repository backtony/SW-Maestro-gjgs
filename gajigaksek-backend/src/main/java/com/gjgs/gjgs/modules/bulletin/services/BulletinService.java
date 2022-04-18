package com.gjgs.gjgs.modules.bulletin.services;

import com.gjgs.gjgs.modules.bulletin.dto.BulletinChangeRecruitResponse;
import com.gjgs.gjgs.modules.bulletin.dto.BulletinDetailResponse;
import com.gjgs.gjgs.modules.bulletin.dto.BulletinIdResponse;
import com.gjgs.gjgs.modules.bulletin.dto.CreateBulletinRequest;
import com.gjgs.gjgs.modules.bulletin.dto.search.BulletinSearchCondition;
import com.gjgs.gjgs.modules.bulletin.dto.search.BulletinSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BulletinService {

    BulletinIdResponse createBulletin(CreateBulletinRequest request);

    BulletinIdResponse deleteBulletin(Long bulletinId);

    BulletinIdResponse modifyBulletin(Long bulletinId, CreateBulletinRequest request);

    Page<BulletinSearchResponse> getBulletins(Pageable pageable, BulletinSearchCondition condition);

    BulletinDetailResponse getBulletinDetails(Long bulletinId);

    BulletinChangeRecruitResponse changeRecruitStatus(Long bulletinId);
}
