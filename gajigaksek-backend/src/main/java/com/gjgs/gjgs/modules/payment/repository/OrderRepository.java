package com.gjgs.gjgs.modules.payment.repository;

import com.gjgs.gjgs.modules.payment.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
