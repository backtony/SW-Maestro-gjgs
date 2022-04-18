package com.gjgs.gjgs.modules.member.dto.mypage;

import com.gjgs.gjgs.modules.member.entity.Member;
import lombok.*;

import javax.persistence.Lob;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DirectorMyPageResponse {

    @Lob
    private String imageFileUrl;

    private String nickname;

    private String name;

    private String phone;

    private String directorText;


    public static DirectorMyPageResponse from(Member member) {
        return DirectorMyPageResponse.builder()
                .imageFileUrl(member.getImageFileUrl())
                .nickname(member.getNickname())
                .name(member.getName())
                .phone(member.getPhone())
                .directorText(member.getDirectorText())
                .build();

    }
}
