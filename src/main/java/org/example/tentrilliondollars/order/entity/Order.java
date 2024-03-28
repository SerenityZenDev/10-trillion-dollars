package org.example.tentrilliondollars.order.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.tentrilliondollars.global.TimeStamped;
import org.example.tentrilliondollars.user.entity.User;


@Getter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order extends TimeStamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * todo : enum으로 변경(배송 전, 배송 중, 배송 후)
     */
    @Enumerated(value = EnumType.STRING)
    private OrderState state;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;


    public Order(User user,OrderState state){
        this.user = user;
        this.state = state;
    }

    public void changeState(OrderState state){
        this.state = state;
    }

}
