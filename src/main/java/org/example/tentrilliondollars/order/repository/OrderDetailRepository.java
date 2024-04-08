
package org.example.tentrilliondollars.order.repository;


import org.example.tentrilliondollars.order.entity.Order;
import org.example.tentrilliondollars.order.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findOrderDetailsByOrder(Order order);
    //review 검증 jpal
    @Query("SELECT COUNT(od) FROM OrderDetail od WHERE od.order.userId= :userId AND od.productId = :productId")
    long countByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    List<OrderDetail> findByOrder(Order order);

}
