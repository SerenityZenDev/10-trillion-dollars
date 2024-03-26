package org.example.tentrilliondollars.order.repository;

import org.example.tentrilliondollars.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
