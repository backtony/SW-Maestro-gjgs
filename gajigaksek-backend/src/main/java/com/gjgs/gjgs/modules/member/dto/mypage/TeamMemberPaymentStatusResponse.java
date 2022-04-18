package com.gjgs.gjgs.modules.member.dto.mypage;

import com.gjgs.gjgs.modules.payment.enums.OrderStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Getter @Setter @NoArgsConstructor(access = PRIVATE) @AllArgsConstructor(access = PRIVATE) @Builder
public class TeamMemberPaymentStatusResponse {

    @Builder.Default
    private List<MemberPaymentStatus> memberStatusList = new ArrayList<>();

    public static TeamMemberPaymentStatusResponse of(List<MemberPaymentStatus> memberStatusList) {
        return TeamMemberPaymentStatusResponse.builder()
                .memberStatusList(memberStatusList)
                .build();
    }

    @Getter @Setter @NoArgsConstructor(access = PRIVATE) @AllArgsConstructor(access = PRIVATE) @Builder
    @EqualsAndHashCode(of = "nickname")
    public static class MemberPaymentStatus {
        private String nickname;
        private String orderState;

        @QueryProjection
        public MemberPaymentStatus(String nickname, OrderStatus orderStatus) {
            this.nickname = nickname;
            this.orderState = orderStatus.name();
        }
    }
}
