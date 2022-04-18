package com.gjgs.gjgs.modules.member.dto.mypage;


import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.enums.Sex;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Lob;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class InfoResponse {

    @Lob
    private String imageFileUrl;

    private Sex sex;

    private int age;

    private Long zoneId;

    @Builder.Default
    private List<Long> categoryIdList = new ArrayList<>();

    private String profileText;

    private String nickname;

    @QueryProjection
    public InfoResponse(String imageFileUrl, Sex sex, int age, Long zoneId, List<Long> categoryIdList, String profileText, String nickname) {
        this.imageFileUrl = imageFileUrl;
        this.sex = sex;
        this.age = age;
        this.zoneId = zoneId;
        this.categoryIdList = categoryIdList;
        this.profileText = profileText;
        this.nickname = nickname;
    }

    public static InfoResponse from(Member member) {
        return InfoResponse.builder()
                .imageFileUrl(member.getImageFileUrl())
                .sex(member.getSex())
                .age(member.getAge())
                .zoneId(member.getZone().getId())
                .categoryIdList(member.getMemberCategories().stream()
                        .map(mc -> mc.getCategory().getId()).collect(Collectors.toList()))
                .profileText(member.getProfileText())
                .nickname(member.getNickname())
                .build();
    }
}
