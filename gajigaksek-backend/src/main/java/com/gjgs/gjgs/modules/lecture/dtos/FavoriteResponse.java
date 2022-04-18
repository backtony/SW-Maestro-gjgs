package com.gjgs.gjgs.modules.lecture.dtos;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
public abstract class FavoriteResponse {

    protected boolean myFavorite;

    abstract public void changeMyFavoriteContents(List<Long> favoriteContentsIdList);
}
