package com.gjgs.gjgs.modules.member.dto.myinfo;

import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DirectorTextModifyRequest {

    @Length(max = 1000,message = "1000자 이하로 작성해야 합니다.")
    private String directorText;

    public static DirectorTextModifyRequest from(String directorText) {
        return DirectorTextModifyRequest.builder()
                .directorText(directorText)
                .build();
    }
}
