package com.gjgs.gjgs.modules.member.dto.search;

import com.gjgs.gjgs.modules.member.enums.Authority;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class MemberSearchResponse {

    private Long id;

    private String nickname;

    private String phone;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    private LocalDateTime createdDate;

    @QueryProjection
    public MemberSearchResponse(Long id, String nickname, String phone, Authority authority, LocalDateTime createdDate) {
        this.id = id;
        this.nickname = nickname;
        this.phone = phone;
        this.authority = authority;
        this.createdDate = createdDate;
    }
}
