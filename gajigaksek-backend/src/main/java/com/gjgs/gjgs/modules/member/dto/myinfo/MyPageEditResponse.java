package com.gjgs.gjgs.modules.member.dto.myinfo;


import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.enums.Authority;
import com.gjgs.gjgs.modules.member.enums.Sex;
import lombok.*;

import javax.persistence.Lob;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPageEditResponse {

    @Lob
    private String imageFileUrl;

    private String nickname;

    private String name;

    private String phone;

    @Builder.Default
    private List<Long> memberCategoryId = new ArrayList<>();

    private Authority authority;

    private String profileText;

    private String directorText;

    private Sex sex;

    private int age;

    private Long zoneId;

    public static MyPageEditResponse from(Member member) {
        return MyPageEditResponse.builder()
                .imageFileUrl(member.getImageFileUrl())
                .nickname(member.getNickname())
                .name(member.getName())
                .phone(member.getPhone())
                .memberCategoryId(member.getMemberCategories().stream()
                        .map(mc -> mc.getCategory().getId())
                        .collect(Collectors.toList()))
                .authority(member.getAuthority())
                .profileText(member.getProfileText())
                .directorText(member.getDirectorText())
                .sex(member.getSex())
                .age(member.getAge())
                .zoneId(member.getZone().getId())
                .build();
    }


}
