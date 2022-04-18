package com.gjgs.gjgs.modules.member.service.search.interfaces;

import com.gjgs.gjgs.modules.member.dto.search.MemberSearchCondition;
import com.gjgs.gjgs.modules.member.dto.search.MemberSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberSearchService {

    Page<MemberSearchResponse> searchMember(Pageable pageable, MemberSearchCondition cond);
}
