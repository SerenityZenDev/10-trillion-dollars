package org.example.tentrilliondollars.order.service;

import lombok.RequiredArgsConstructor;
import org.example.tentrilliondollars.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

}
