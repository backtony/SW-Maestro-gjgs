package com.gjgs.gjgs.modules.member.dto.login;


import lombok.*;

import javax.persistence.Lob;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequest {

    // 카카오
    @NotNull(message = "id값은 반드시 입력되어야 하는 값입니다.")
    private Long id;

    @Lob
    @NotBlank(message = "imageFileUrl은 반드시 입력되어야 하는 값입니다.")
    private String imageFileUrl;


    @Pattern(regexp = "^[가-힣]{1,10}$",message = "실명은 한글로 1 ~ 10 글자사이의 값이어야 합니다.")
    private String name;

    // 숫자 01000000000 // 3 + 4 + 4
    // 숫자 0100000000 // 3 + 3 + 4
    @Pattern(regexp = "\\d{10,11}",message = "휴대폰 번호는 숫자로 10 ~ 11자리의 값이어야 합니다.")
    private String phone;

    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9_-]{2,20}$",message = "닉네임은 한글,영문,숫자를 포함한 2 ~ 20 글자 사이여야 합니다.")
    private String nickname;

    @Min(value = 10, message = "최소 10세 이상이어야 합니다.")
    @Max(value = 100, message = "최대 100세 이하이어야 합니다.")
    private int age;


    @Pattern(regexp = "^[FM]{1,1}$",message = "성별은 M 또는 F 이어야 합니다.")
    private String sex;

    @NotNull(message = "지역을 선택해주세요.")
    private Long zoneId;

    @NotEmpty(message = "선호하는 취미를 선택해주세요.")
    @Builder.Default
    @Size(min = 1, max = 3, message = "선호하는 카테고리는 최소 1개 최대 3개까지만 선택할 수 있습니다.")
    private List<Long> categoryIdList = new ArrayList<>();

    @NotBlank(message = "fcmToken은 반드시 입력되어야 합니다.")
    private String fcmToken;

    private String recommendNickname;
}
