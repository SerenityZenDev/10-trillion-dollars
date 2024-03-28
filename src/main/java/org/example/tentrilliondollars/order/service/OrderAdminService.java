package org.example.tentrilliondollars.order.service;

import lombok.RequiredArgsConstructor;
import org.example.tentrilliondollars.order.entity.OrderState;
import org.example.tentrilliondollars.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.example.tentrilliondollars.order.entity.Order;
@Service
@RequiredArgsConstructor
public class OrderAdminService {

    private final OrderRepository orderRepository;
    public void changeState(int requestState,Long orderId) {
        Order order = orderRepository.getReferenceById(orderId);
        if (requestState == 0) {
            order.changeState(OrderState.PREPARING);
        } else if (requestState == 1) {
            order.changeState(OrderState.SHIPPING);
        } else if (requestState == 2) {
            order.changeState(OrderState.DELIVERED);
        }
        orderRepository.save(order);

    }
}
