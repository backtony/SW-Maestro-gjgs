package com.batch.redisbatch.dummy;

import com.batch.redisbatch.domain.Member;
import com.batch.redisbatch.domain.Order;
import com.batch.redisbatch.domain.Team;
import com.batch.redisbatch.domain.lecture.Schedule;
import com.batch.redisbatch.enums.OrderStatus;

public class OrderDummy {

    public static Order createOrder(Member member, Schedule schedule){
        return Order.builder()
                .orderStatus(OrderStatus.WAIT)
                .member(member)
                .schedule(schedule)
                .originalPrice(4000)
                .discountPrice(2000)
                .finalPrice(2000)
                .iamportUid("iamport")
                .build();
    }

    public static Order createOrder(String uuid, OrderStatus orderStatus, Member member, Schedule schedule, Team team) {
        return Order.builder()
                .orderStatus(orderStatus)
                .team(team)
                .member(member)
                .schedule(schedule)
                .originalPrice(4000)
                .discountPrice(2000)
                .finalPrice(2000)
                .iamportUid("iamport")
                .build();
    }

    public static Order createOrder(OrderStatus orderStatus,Member member, Schedule schedule) {
        return Order.builder()
                .orderStatus(orderStatus)
                .member(member)
                .schedule(schedule)
                .originalPrice(4000)
                .discountPrice(2000)
                .finalPrice(2000)
                .iamportUid("iamport")
                .build();
    }
}
