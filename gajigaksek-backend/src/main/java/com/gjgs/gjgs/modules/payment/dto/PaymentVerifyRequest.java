package com.gjgs.gjgs.modules.payment.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

import static lombok.AccessLevel.PRIVATE;

@Getter @Setter @NoArgsConstructor(access = PRIVATE) @AllArgsConstructor(access = PRIVATE) @Builder
public class PaymentVerifyRequest {

    @NotNull(message = "주문 ID가 필요합니다.")
    private Long orderId;

    @NotNull(message = "아임포트에서 받은 UID가 필요합니다.")
    private String iamportUid;
}
