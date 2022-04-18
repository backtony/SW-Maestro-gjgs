package com.gjgs.gjgs.modules.favorite.dto;


import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteBulletinDtoResponse {

    private List<FavoriteBulletinDto> favoriteBulletinDtoList;

    public static FavoriteBulletinDtoResponse from(List<FavoriteBulletinDto> favoriteBulletinDtoList) {
        return FavoriteBulletinDtoResponse.builder()
                .favoriteBulletinDtoList(favoriteBulletinDtoList)
                .build();
    }
}
