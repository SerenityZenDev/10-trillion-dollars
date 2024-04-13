
package org.example.tentrilliondollars.order.repository;


import org.example.tentrilliondollars.order.entity.Order;
import org.example.tentrilliondollars.order.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    //review 검증 jpal
    @Query("SELECT COUNT(od) FROM OrderDetail od WHERE od.orderId= :userId AND od.productId = :productId")
    long countByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);


    //관리자 페이지에 주문서를 불러오는 쿼리
    List<OrderDetail> findByProductId(Long productId);


    //연관관계
    List<OrderDetail> findOrderDetailsByOrderId(Long orderId);
    List<OrderDetail> findByOrderId(Long orderId);

}
