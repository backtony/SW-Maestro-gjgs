package com.gjgs.gjgs.modules.member.service;

import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.member.dto.search.MemberSearchCondition;
import com.gjgs.gjgs.modules.member.dto.search.MemberSearchResponse;
import com.gjgs.gjgs.modules.member.enums.Authority;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberQueryRepository;
import com.gjgs.gjgs.modules.member.service.search.impl.MemberSearchServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberSearchServiceImplTest {

    @InjectMocks MemberSearchServiceImpl memberSearchService;
    @Mock MemberQueryRepository memberQueryRepository;

    @DisplayName("con, pageable 정보로 member search")
    @Test
    void search_member() throws Exception{
        //given
        PageRequest pageRequest = PageRequest.of(0, 20, Sort.Direction.DESC, "createdDate");
        MemberSearchCondition condition = createMemberSearchCondition();
        PageImpl<MemberSearchResponse> pagingMemberSearchResponse = createPagingMemberSearchResponse(pageRequest);

        when(memberQueryRepository.findPagingMemberByCondition(any(),any())).thenReturn(pagingMemberSearchResponse);

        //when
        Page<MemberSearchResponse> memberSearchDtos = memberSearchService.searchMember(pageRequest, condition);

        //then
        assertEquals(pagingMemberSearchResponse,memberSearchDtos);
    }

    private PageImpl<MemberSearchResponse> createPagingMemberSearchResponse(PageRequest pageRequest) {
        return new PageImpl<>(MemberDummy.createMemberSearchDtoList(25),
                pageRequest,
                25);
    }

    private MemberSearchCondition createMemberSearchCondition() {
        return MemberSearchCondition.builder()
                .nickname("mem")
                .authority(Authority.ROLE_USER)
                .createdDateStart(LocalDate.now().minusDays(1))
                .createdDateEnd(LocalDate.now())
                .build();
    }
}
