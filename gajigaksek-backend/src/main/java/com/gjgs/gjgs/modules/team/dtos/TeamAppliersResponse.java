package com.gjgs.gjgs.modules.team.dtos;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class TeamAppliersResponse {

    @Builder.Default
    private Set<TeamApplierResponse> applierList = new LinkedHashSet<>();

    public static TeamAppliersResponse from(Set<TeamApplierResponse> response) {
        return TeamAppliersResponse.builder().applierList(response).build();
    }

    @Getter @Setter @NoArgsConstructor(access = AccessLevel.PROTECTED) @Builder
    @EqualsAndHashCode(of = "memberId")
    public static class TeamApplierResponse {
        private Long memberId;
        private String thumbnailImageUrl;
        private String nickname;
        private String sex;
        private int age;

        @QueryProjection
        public TeamApplierResponse(Long memberId, String thumbnailImageUrl, String nickname, String sex, int age) {
            this.memberId = memberId;
            this.thumbnailImageUrl = thumbnailImageUrl;
            this.nickname = nickname;
            this.sex = sex;
            this.age = age;
        }
    }
}
