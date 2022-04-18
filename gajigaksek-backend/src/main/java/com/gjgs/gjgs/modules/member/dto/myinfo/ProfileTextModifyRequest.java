package com.gjgs.gjgs.modules.member.dto.myinfo;

import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileTextModifyRequest {

    @Length(max = 1000,message = "1000자 이하로 작성해야 합니다.")
    private String profileText;

    public static ProfileTextModifyRequest from(String profileText) {
        return ProfileTextModifyRequest.builder()
                .profileText(profileText)
                .build();
    }
}

