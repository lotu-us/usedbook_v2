package team.hello.usedbook.domain;

import lombok.Getter;
import lombok.ToString;
import team.hello.usedbook.domain.enums.OrderStatus;
import team.hello.usedbook.domain.enums.Payment;

@Getter @ToString
public class Orders {
    private Long id;
    private String orderId;
    private Long memberId;
    private OrderStatus orderStatus;
    private Payment payment;
    private String orderTime;


    public Orders(String orderId, Long memberId, OrderStatus orderStatus, Payment payment, String orderTime) {
        this.orderId = orderId;
        this.memberId = memberId;
        this.orderStatus = orderStatus;
        this.payment = payment;
        this.orderTime = orderTime;
    }
}
