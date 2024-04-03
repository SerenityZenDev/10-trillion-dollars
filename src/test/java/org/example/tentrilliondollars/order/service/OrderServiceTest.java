package org.example.tentrilliondollars.order.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;
import org.example.tentrilliondollars.address.entity.Address;
import org.example.tentrilliondollars.global.security.UserDetailsImpl;
import org.example.tentrilliondollars.product.entity.Product;
import org.example.tentrilliondollars.product.repository.ProductRepository;
import org.example.tentrilliondollars.user.entity.User;
import org.example.tentrilliondollars.user.entity.UserRoleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;


@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
public class OrderServiceTest {

    @MockBean
    private ProductRepository productRepository;
    @Autowired
    private OrderService orderService;
    UserDetailsImpl userDetails;
    User user;
    Product product;
    Address address;
    Map<Long, Long> basket = new HashMap<>();

    @BeforeEach
    //public void createOrder(Map<Long, Long> basket, UserDetailsImpl userDetails, Long addressId)
    void set() {
        user = new User(1L, "tester", "test@test.com", UserRoleEnum.USER);
        userDetails = new UserDetailsImpl(user);
        product = new Product(1L, "tester", 3000L, "test@test.com", 200L, "photo", user);
        productRepository.save(product);
        address = new Address(1L, "test", "test", "test", user);
        basket.put(1L,1L);
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // productRepository.getReferenceById가 호출될 때 product 객체를 반환하도록 설정
        Mockito.when(productRepository.getReferenceById(1L)).thenReturn(product);
    }

    @Test
    @DisplayName("병렬로 100번 실행하여 재고 업데이트 테스트")
    void test(){
        IntStream.range(0, 100).parallel().forEach(i -> {
            try {
                orderService.createOrder(basket,userDetails,address.getId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
//    @Test
//    @DisplayName("100번 실행하여 재고 업데이트 테스트")
//    void test2(){
//        IntStream.range(0, 100).parallel().forEach(i -> {
//            try {
//                orderService.createOrdeOg(basket,userDetails,address.getId());
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        });
//    }
}
