package org.example.tentrilliondollars.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.tentrilliondollars.order.entity.OrderDetail;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class OrderResponseDto {
    private Long productId;
    private Long quantity;


    public OrderResponseDto(OrderDetail orderDetail){
        this.productId = orderDetail.getProductId();
        this.quantity = orderDetail.getQuantity();

    }

}