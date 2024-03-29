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
public class OrderResponseDto {
    private Long productId;
    private OrderState state;

    public OrderResponseDto(Order order){
       this.productId = order.getId();
       this.state = order.getState();
    }
}
