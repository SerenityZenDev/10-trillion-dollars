package org.example.tentrilliondollars.order.controller;


import lombok.RequiredArgsConstructor;
import org.example.tentrilliondollars.order.service.OrderService;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

}
