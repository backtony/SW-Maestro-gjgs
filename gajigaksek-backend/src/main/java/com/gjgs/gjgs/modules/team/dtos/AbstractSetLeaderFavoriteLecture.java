package com.gjgs.gjgs.modules.team.dtos;

import java.util.List;

public abstract class AbstractSetLeaderFavoriteLecture {

    abstract public void setLeaderMyFavoriteLecture(Long memberId, List<Long> favoriteLectureIdList);
}
