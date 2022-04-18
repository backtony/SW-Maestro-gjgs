package com.gjgs.gjgs.modules.bulletin.dto.search;

import com.gjgs.gjgs.modules.utils.validators.search.SearchKeyword;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BulletinSearchCondition implements SearchKeyword {

    private String keyword;

    @Builder.Default
    private List<Long> categoryIdList = new ArrayList<>();

    private Long zoneId;
}
