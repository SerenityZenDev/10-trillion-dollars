package org.example.tentrilliondollars.order.service;

import lombok.RequiredArgsConstructor;
import org.example.tentrilliondollars.address.repository.AddressRepository;
import org.example.tentrilliondollars.address.service.AddressService;
import org.example.tentrilliondollars.global.security.UserDetailsImpl;
import org.example.tentrilliondollars.order.dto.OrderDetailResponseDto;
import org.example.tentrilliondollars.order.dto.OrderResponseDto;
import org.example.tentrilliondollars.order.entity.Order;
import org.example.tentrilliondollars.order.entity.OrderDetail;
import org.example.tentrilliondollars.order.entity.OrderState;
import org.example.tentrilliondollars.order.repository.OrderDetailRepository;
import org.example.tentrilliondollars.order.repository.OrderRepository;
import org.example.tentrilliondollars.product.entity.Product;
import org.example.tentrilliondollars.product.repository.ProductRepository;
import org.example.tentrilliondollars.product.service.ProductService;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductService productService;
    private final AddressService addressService;

    @Transactional
    public void createOrder(Map<Long,Long> basket,UserDetailsImpl userDetails,Long addressId) throws Exception {
        checkBasket(basket);
        Order order = new Order(userDetails.getUser().getId(),OrderState.NOTPAYED, addressService.findOne(addressId));
        orderRepository.save(order);
        for(Long key:basket.keySet()){
            OrderDetail orderDetail= new OrderDetail(order,key,basket.get(key),productService.getProduct(key).getPrice(),productService.getProduct(key).getName());
            orderDetailRepository.save(orderDetail);
            updateStock(key,basket.get(key));
        }

    }
    public List<OrderDetailResponseDto> getOrderDetailList(Long orderId){
        List<OrderDetail> listOfOrderedProducts = orderDetailRepository.findOrderDetailsByOrder(orderRepository.getById(orderId));
        return listOfOrderedProducts.stream().map(OrderDetailResponseDto::new).toList();
    }
    @Transactional
    public void deleteOrder(Long orderId){
        orderDetailRepository.deleteAll(orderDetailRepository.findOrderDetailsByOrder(orderRepository.getById(orderId)));
        orderRepository.delete(orderRepository.getById(orderId));
    }

    public boolean checkUser(UserDetailsImpl userDetails,Long orderId){
        return Objects.equals(userDetails.getUser().getId(), orderRepository.getById(orderId).getUserId());
    }

    public void updateStock(Long productId,Long quantity) throws ChangeSetPersister.NotFoundException {
        Product product =  productService.getProduct(productId);
        product.updateStockAfterOrder(quantity);

    }

    public boolean checkStock(Long productId,Long quantity) throws ChangeSetPersister.NotFoundException {
        return productService.getProduct(productId).getStock() - quantity >= 0;
    }

    public List<OrderResponseDto> getOrderList(UserDetailsImpl userDetails){
        List<Order> orderList = orderRepository.findOrdersByUserId(userDetails.getUser().getId());
        return orderList.stream().map(OrderResponseDto::new).toList();
    }

    public void checkBasket(Map<Long,Long> basket) throws Exception {
        for(Long key:basket.keySet()){
            if(!checkStock(key,basket.get(key))){throw new Exception("id:"+key+" 수량부족");}
        }
    }

    public Order getOrder(Long orderId){
        return orderRepository.getById(orderId);
    }

}
