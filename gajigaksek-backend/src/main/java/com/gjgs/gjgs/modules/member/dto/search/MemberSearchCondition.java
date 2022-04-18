package com.gjgs.gjgs.modules.member.dto.search;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.gjgs.gjgs.modules.member.enums.Authority;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberSearchCondition {

    private String nickname;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdDateStart;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdDateEnd;

    private Authority authority;

}

