package com.gjgs.gjgs.modules.member.dto.myinfo;

import com.gjgs.gjgs.modules.member.validator.CheckIsDuplicateNickname;
import lombok.*;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NicknameModifyRequest {

    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9_-]{2,20}$",message = "닉네임은 한글/영어(대,소문자)/숫자 조합으로 2 ~ 20자")
    @CheckIsDuplicateNickname
    private String nickname;
}
