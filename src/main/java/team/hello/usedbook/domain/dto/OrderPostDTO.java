package team.hello.usedbook.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

public class OrderPostDTO {

    @Data
    @NoArgsConstructor
    public static class Response{
        private Long id;
        private String orderId;
        private Long postId;
        private String writer;
        private String title;
        private int price;
        private int count;
    }
}
