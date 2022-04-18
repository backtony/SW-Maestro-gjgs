package com.gjgs.gjgs.modules.member.controller;


import com.gjgs.gjgs.modules.member.dto.search.MemberSearchCondition;
import com.gjgs.gjgs.modules.member.dto.search.MemberSearchResponse;
import com.gjgs.gjgs.modules.member.service.search.interfaces.MemberSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class MemberSearchController {

    private final MemberSearchService memberSearchService;

    @GetMapping
    public ResponseEntity<Page<MemberSearchResponse>> searchMember(
                                        @PageableDefault(size=20,sort = "createdDate", direction = Sort.Direction.DESC)
                                        Pageable pageable,
                                        MemberSearchCondition memberSearchCondition){
        return ResponseEntity.ok(memberSearchService.searchMember(pageable,memberSearchCondition));
    }
}
