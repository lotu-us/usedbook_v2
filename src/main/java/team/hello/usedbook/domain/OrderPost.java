package team.hello.usedbook.domain;

import lombok.Getter;
import lombok.ToString;

@Getter @ToString
public class OrderPost {
    private Long id;
    private String orderId;
    private Long postId;
    private int count;

    public OrderPost(Long postId, int count) {
        this.postId = postId;
        this.count = count;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
