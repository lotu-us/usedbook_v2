package team.hello.usedbook.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import team.hello.usedbook.domain.enums.OrderStatus;

import java.time.LocalDateTime;

@Getter @ToString
@NoArgsConstructor @AllArgsConstructor
public class Order {
    private Long id;
    private Long memberId;
    private Long postId;
    private int orderCount;
    private OrderStatus orderStatus;
    private LocalDateTime orderTime;
}
