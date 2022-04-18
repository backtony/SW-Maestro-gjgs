package com.gjgs.gjgs.modules.favorite.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyTeamAndIsIncludeFavoriteLectureDtoResponse {

    private List<MyTeamAndIsIncludeFavoriteLectureDto> myTeamAndIsIncludeFavoriteLectureDtoList;

    public static MyTeamAndIsIncludeFavoriteLectureDtoResponse from
            (List<MyTeamAndIsIncludeFavoriteLectureDto> myTeamAndIsIncludeFavoriteLectureDtoList) {

        return MyTeamAndIsIncludeFavoriteLectureDtoResponse.builder()
                .myTeamAndIsIncludeFavoriteLectureDtoList(myTeamAndIsIncludeFavoriteLectureDtoList)
                .build();
    }
}
