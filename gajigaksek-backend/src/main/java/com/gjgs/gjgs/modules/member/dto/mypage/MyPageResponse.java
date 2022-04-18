package com.gjgs.gjgs.modules.member.dto.mypage;

import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.enums.Authority;
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
public class MyPageResponse {
    private String nickname;

    @Lob
    private String imageFileUrl;

    @Builder.Default
    private List<Long> memberCategoryIdList = new ArrayList<>();

    private Authority authority;

    private int totalReward;

    public static MyPageResponse from(Member member) {
        return MyPageResponse.builder()
                .nickname(member.getNickname())
                .imageFileUrl(member.getImageFileUrl())
                .authority(member.getAuthority())
                .memberCategoryIdList(member.getMemberCategories().stream()
                        .map(mc -> mc.getCategory().getId())
                        .collect(Collectors.toList()))
                .totalReward(member.getTotalReward())
                .build();
    }

}
