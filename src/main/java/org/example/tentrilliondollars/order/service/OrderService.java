package org.example.tentrilliondollars.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sparta.example.finalproject.order.repository.OrderRepository;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

}
