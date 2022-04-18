package com.gjgs.gjgs.modules.member.dto.myinfo;

import com.gjgs.gjgs.modules.member.validator.CheckIsDuplicatePhone;
import lombok.*;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhoneModifyRequest {

    @Pattern(regexp = "\\d{10,11}",message = "숫자로 [10,11]자만 가능")
    @CheckIsDuplicatePhone
    private String phone;

}
