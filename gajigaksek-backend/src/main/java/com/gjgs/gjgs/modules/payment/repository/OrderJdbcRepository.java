package com.gjgs.gjgs.modules.payment.repository;

import com.gjgs.gjgs.modules.payment.entity.Order;

import java.util.List;

public interface OrderJdbcRepository {

    void insertOrders(List<Order> orderList);
}
