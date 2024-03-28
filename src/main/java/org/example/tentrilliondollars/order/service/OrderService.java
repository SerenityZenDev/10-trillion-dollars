package org.example.tentrilliondollars.order.service;

import lombok.RequiredArgsConstructor;
import org.example.tentrilliondollars.global.security.UserDetailsImpl;
import org.example.tentrilliondollars.order.dto.OrderResponseDto;
import org.example.tentrilliondollars.order.entity.Order;
import org.example.tentrilliondollars.order.entity.OrderDetail;
import org.example.tentrilliondollars.order.entity.OrderState;
import org.example.tentrilliondollars.order.repository.OrderDetailRepository;
import org.example.tentrilliondollars.order.repository.OrderRepository;
import org.example.tentrilliondollars.product.entity.Product;
import org.example.tentrilliondollars.product.repository.ProductRepository;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;

    public Order createOrder(UserDetailsImpl userDetails){
        Order order = new Order(userDetails.getUser(),OrderState.PREPARING);
        return orderRepository.save(order);
    }
    @Transactional
    public void saveOrderDetails(Map<String,Long> basket,Order order){
        for(String key:basket.keySet()){
            Long price = productRepository.findProductByName(key).getPrice();
            OrderDetail orderDetail= new OrderDetail(order,key,basket.get(key),price);
            orderDetailRepository.save(orderDetail);
            updateStock(key,basket.get(key));
        }

    }
    public List<OrderResponseDto> getOrderDetailList(Long orderId){
        List<OrderDetail> listOfOrderedProducts = orderDetailRepository.findOrderDetailsByOrder(orderRepository.getReferenceById(orderId));
        return listOfOrderedProducts.stream().map(OrderResponseDto::new).toList();
    }
    @Transactional
    public void deleteOrder(Long orderId){
        orderDetailRepository.deleteAll(orderDetailRepository.findOrderDetailsByOrder(orderRepository.getReferenceById(orderId)));
        orderRepository.delete(orderRepository.getReferenceById(orderId));
    }

    public boolean checkUser(UserDetailsImpl userDetails,Long orderId){
        return Objects.equals(userDetails.getUser().getId(), orderRepository.getReferenceById(orderId).getUser().getId());
    }

    public void updateStock(String product_name,Long quantity){
        Product product =  productRepository.findProductByName(product_name);
        product.updateStockAfterOrder(quantity);

    }

}
