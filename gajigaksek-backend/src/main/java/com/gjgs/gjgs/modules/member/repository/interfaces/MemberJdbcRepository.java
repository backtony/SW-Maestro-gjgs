package com.gjgs.gjgs.modules.member.repository.interfaces;

import java.util.List;

public interface MemberJdbcRepository {

    void insertMemberCategoryList(Long memberId, List<Long> changeCategoryIdList);
}
