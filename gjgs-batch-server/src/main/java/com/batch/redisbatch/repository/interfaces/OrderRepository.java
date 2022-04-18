package com.batch.redisbatch.repository.interfaces;

import com.batch.redisbatch.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
