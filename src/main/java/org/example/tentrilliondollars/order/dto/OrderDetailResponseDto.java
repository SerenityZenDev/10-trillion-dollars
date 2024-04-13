package org.example.tentrilliondollars.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.tentrilliondollars.order.entity.Order;
import org.example.tentrilliondollars.order.entity.OrderDetail;
import org.example.tentrilliondollars.order.entity.OrderState;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class OrderDetailResponseDto {
    private Long productId;
    private Long quantity;
    //private OrderState state;
    private String productName;


    public OrderDetailResponseDto(OrderDetail orderDetail){
        this.productId = orderDetail.getProductId();
        this.quantity = orderDetail.getQuantity();
        //this.state = order.getState();
        this.productName = orderDetail.getProductName();
    }

}
