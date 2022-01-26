package team.hello.usedbook.domain;

import lombok.Getter;
import lombok.ToString;
import team.hello.usedbook.domain.enums.OrderStatus;
import team.hello.usedbook.domain.enums.Payment;

@Getter @ToString
public class Orders {
    private Long id;
    private Long memberId;
    private String orderId;
    private OrderStatus status;
    private Payment payment;
    private String orderTime;


    public Orders(Long memberId, String orderId, OrderStatus status, Payment payment, String orderTime) {
        this.memberId = memberId;
        this.orderId = orderId;
        this.status = status;
        this.payment = payment;
        this.orderTime = orderTime;
    }

    public Orders(Long id, Long memberId, String orderId, OrderStatus status, Payment payment, String orderTime) {
        this.id = id;
        this.memberId = memberId;
        this.orderId = orderId;
        this.status = status;
        this.payment = payment;
        this.orderTime = orderTime;
    }
}
