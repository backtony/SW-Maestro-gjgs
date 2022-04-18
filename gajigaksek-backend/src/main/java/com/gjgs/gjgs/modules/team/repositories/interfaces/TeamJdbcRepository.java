package com.gjgs.gjgs.modules.team.repositories.interfaces;

import java.util.List;

public interface TeamJdbcRepository {

    void insertTeamCategoryList(Long teamId, List<Long> categoryIdList);

    void insertMemberTeamList(Long teamId, List<Long> memberIdList);
}
