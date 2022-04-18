package com.gjgs.gjgs.modules.favorite.service.interfaces;

import com.gjgs.gjgs.modules.favorite.dto.FavoriteBulletinDto;
import com.gjgs.gjgs.modules.favorite.dto.LectureMemberDto;
import com.gjgs.gjgs.modules.favorite.dto.LectureTeamDto;
import com.gjgs.gjgs.modules.favorite.dto.MyTeamAndIsIncludeFavoriteLectureDto;

import java.util.List;

public interface FavoriteService {
    List<LectureMemberDto> getMyFavoriteLectures();

    void deleteMyFavoriteLecture(Long lectureId);

    List<LectureTeamDto> getTeamFavoriteLectures(Long teamId);

    void deleteTeamFavoriteLecture(Long teamId, Long lectureId);

    List<FavoriteBulletinDto> getMyFavoriteBulletins();

    void deleteMyFavoriteBulletin(Long bulletinId);

    void saveMyFavoriteLecture(Long lectureId);

    void saveTeamFavoriteLecture(Long teamId, Long lectureId);

    void saveMyFavoriteBulletin(Long bulletinId);

    List<MyTeamAndIsIncludeFavoriteLectureDto> getMyTeamAndIsIncludeFavoriteLecture(Long lectureId);
}
