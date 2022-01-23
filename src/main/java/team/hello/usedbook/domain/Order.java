package team.hello.usedbook.domain;

import lombok.Getter;
import lombok.ToString;
import team.hello.usedbook.domain.enums.OrderStatus;

@Getter @ToString
public class Order {
    private Long id;
    private String orderId;
    private Long memberId;
    private Long postId;
    private int count;
    private OrderStatus orderStatus;
    private String orderTime;
    private String payment;

//    private Long postcode;
//    private String address;
//    private String detailAddress;
//    private String extraAddress;

    //basket to order
    public Order(String orderId, Long memberId, Long postId, int count, OrderStatus orderStatus, String orderTime) {
        this.orderId = orderId;
        this.memberId = memberId;
        this.postId = postId;
        this.count = count;
        this.orderStatus = orderStatus;
        this.orderTime = orderTime;
    }

}
