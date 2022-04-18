package com.gjgs.gjgs.modules.favorite.controller;

import com.gjgs.gjgs.modules.favorite.dto.FavoriteBulletinDtoResponse;
import com.gjgs.gjgs.modules.favorite.dto.LectureMemberDtoResponse;
import com.gjgs.gjgs.modules.favorite.dto.LectureTeamDtoResponse;
import com.gjgs.gjgs.modules.favorite.dto.MyTeamAndIsIncludeFavoriteLectureDtoResponse;
import com.gjgs.gjgs.modules.favorite.service.interfaces.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping("/my-teams/info/{lectureId}")
    public ResponseEntity<MyTeamAndIsIncludeFavoriteLectureDtoResponse> getMyTeamAndIsIncludeFavoriteLecture
                                                                    (@PathVariable Long lectureId) {

        return ResponseEntity.ok(MyTeamAndIsIncludeFavoriteLectureDtoResponse
                .from(favoriteService.getMyTeamAndIsIncludeFavoriteLecture(lectureId)));
    }


    @PostMapping("/lectures/{lectureId}")
    public ResponseEntity<Void> saveMyFavoriteLecture(@PathVariable Long lectureId) {
        favoriteService.saveMyFavoriteLecture(lectureId);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/lectures")
    public ResponseEntity<LectureMemberDtoResponse> getMyFavoriteLectures() {
        return ResponseEntity.ok(LectureMemberDtoResponse.from(favoriteService.getMyFavoriteLectures()));
    }

    @DeleteMapping("/lectures/{lectureId}")
    public ResponseEntity<Void> deleteMyFavoriteLecture(@PathVariable Long lectureId) {
        favoriteService.deleteMyFavoriteLecture(lectureId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/teams/{teamId}/{lectureId}")
    public ResponseEntity<Void> saveTeamFavoriteLecture(@PathVariable("teamId") Long teamId,
                                                        @PathVariable("lectureId") Long lectureId) {
        favoriteService.saveTeamFavoriteLecture(teamId, lectureId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/teams/{teamId}")
    public ResponseEntity<LectureTeamDtoResponse> getTeamFavoriteLectures(@PathVariable Long teamId) {
        return ResponseEntity.ok(LectureTeamDtoResponse.from(favoriteService.getTeamFavoriteLectures(teamId)));
    }

    @DeleteMapping("/teams/{teamId}/{lectureId}")
    public ResponseEntity<Void> deleteTeamFavoriteLecture(@PathVariable("teamId") Long teamId,
                                                          @PathVariable("lectureId") Long lectureId) {

        favoriteService.deleteTeamFavoriteLecture(teamId, lectureId);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/bulletins/{bulletinId}")
    public ResponseEntity<Void> saveMyFavoriteBulletin(@PathVariable Long bulletinId) {
        favoriteService.saveMyFavoriteBulletin(bulletinId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/bulletins")
    public ResponseEntity<FavoriteBulletinDtoResponse> getMyFavoriteBulletins() {
        return ResponseEntity.ok(FavoriteBulletinDtoResponse.from(favoriteService.getMyFavoriteBulletins()));
    }

    @DeleteMapping("/bulletins/{bulletinId}")
    public ResponseEntity<Void> deleteMyFavoriteBulletin(@PathVariable Long bulletinId) {
        favoriteService.deleteMyFavoriteBulletin(bulletinId);
        return ResponseEntity.ok().build();
    }


}
