package com.gjgs.gjgs.modules.payment.dto;

import lombok.*;

import static lombok.AccessLevel.PRIVATE;

@Getter @Setter @NoArgsConstructor(access = PRIVATE) @AllArgsConstructor(access = PRIVATE)  @Builder
public class OrderIdDto {

    private Long orderId;
    private String description;

    public static OrderIdDto ofComplete(Long orderId) {
        return OrderIdDto.builder()
                .description("결제가 정상적으로 완료되었습니다.")
                .orderId(orderId)
                .build();
    }

    public static OrderIdDto ofCancel(Long orderId) {
        return OrderIdDto.builder()
                .description("결제 금액이 맞지 않아 취소되었습니다.")
                .orderId(orderId)
                .build();
    }
}
