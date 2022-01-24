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
    private Long postId;
    private int count;
    private OrderStatus orderStatus;
    private Payment payment;
    private String orderTime;


//    private Long postcode;
//    private String address;
//    private String detailAddress;
//    private String extraAddress;

    //basket to order
    public Orders(String orderId, Long memberId, Long postId, int count, OrderStatus orderStatus, Payment payment, String orderTime) {
        this.orderId = orderId;
        this.memberId = memberId;
        this.postId = postId;
        this.count = count;
        this.orderStatus = orderStatus;
        this.payment = payment;
        this.orderTime = orderTime;
    }

}