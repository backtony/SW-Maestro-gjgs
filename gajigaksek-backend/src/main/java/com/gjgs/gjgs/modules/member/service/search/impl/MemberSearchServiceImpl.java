package com.gjgs.gjgs.modules.member.service.search.impl;

import com.gjgs.gjgs.modules.member.dto.search.MemberSearchCondition;
import com.gjgs.gjgs.modules.member.dto.search.MemberSearchResponse;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberQueryRepository;
import com.gjgs.gjgs.modules.member.service.search.interfaces.MemberSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class MemberSearchServiceImpl implements MemberSearchService {

    private final MemberQueryRepository memberQueryRepository;

    @Override
    public Page<MemberSearchResponse> searchMember(Pageable pageable, MemberSearchCondition cond) {
        return memberQueryRepository.findPagingMemberByCondition(pageable,cond);
    }
}
