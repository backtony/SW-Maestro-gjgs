package com.gjgs.gjgs.modules.member.dto.myinfo;

import com.gjgs.gjgs.modules.member.validator.CheckIsExistCategory;
import lombok.*;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryModifyRequest {

    @Builder.Default
    @Size(min = 1, max = 3)
    @CheckIsExistCategory
    private List<Long> categoryIdList = new ArrayList<>();

}
