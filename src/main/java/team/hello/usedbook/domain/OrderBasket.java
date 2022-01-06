package team.hello.usedbook.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter @ToString
@NoArgsConstructor @AllArgsConstructor
public class OrderBasket {
    private Long id;
    private Long memberId;
    private Long postId;
    private int orderCount;
}
