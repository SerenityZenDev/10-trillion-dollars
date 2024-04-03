package org.example.tentrilliondollars.order.service;


import static org.example.tentrilliondollars.order.entity.QOrder.order;

import lombok.RequiredArgsConstructor;
import org.example.tentrilliondollars.global.config.RedisConfig;
import org.example.tentrilliondollars.order.entity.Order;
import org.example.tentrilliondollars.order.entity.OrderDetail;
import org.example.tentrilliondollars.order.repository.OrderDetailRepository;
import org.example.tentrilliondollars.product.entity.Product;
import org.example.tentrilliondollars.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockService {
    private final ProductRepository productRepository;
    private final RedisConfig redisConfig;
    private final OrderDetailRepository orderDetailRepository;

//@Transactional
//public void updateStock(Order order, Long productId, Long quantity) {
//    final String lockKey = "productLock:" + productId;
//    System.out.println("2번");
//    redisConfig.withLock(lockKey, () -> {
//        Product product = productRepository.findById(productId)
//            .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));
//        if (product.getStock() < quantity) {
//            throw new IllegalArgumentException("Not enough stock for product ID: " + productId);
//        }
//        product.updateStockAfterOrder(quantity);
//        System.out.println(product.getStock());
//        OrderDetail orderDetail = new OrderDetail(order, productId, quantity, product.getPrice(), product.getName());
//        orderDetailRepository.save(orderDetail);
//        return null;
//    });
//}
@Transactional
public void updateStock(Order order, Long productId, Long quantity) {
    System.out.println("2번");
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));
    if (product.getStock() < quantity) {
        throw new IllegalArgumentException("Not enough stock for product ID: " + productId);
    }
    product.updateStockAfterOrder(quantity);
    System.out.println(product.getStock());
    OrderDetail orderDetail = new OrderDetail(order, productId, quantity, product.getPrice(), product.getName());
    orderDetailRepository.save(orderDetail);
}
    @Transactional
    public void updateStockAndCreateOrderDetail(Order order, Long productId, Long quantity) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));
        // 재고 업데이트 로직
        product.updateStockAfterOrder(quantity);
        // OrderDetail 생성 및 저장
        OrderDetail orderDetail = new OrderDetail(order, productId, quantity, product.getPrice(), product.getName());
        orderDetailRepository.save(orderDetail);
    }
}


