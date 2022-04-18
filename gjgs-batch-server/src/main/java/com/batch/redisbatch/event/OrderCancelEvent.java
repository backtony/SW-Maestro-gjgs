package com.batch.redisbatch.event;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderCancelEvent {

    private final Long memberId;
}
