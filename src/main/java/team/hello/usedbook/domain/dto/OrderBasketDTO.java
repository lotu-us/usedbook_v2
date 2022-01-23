package team.hello.usedbook.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import team.hello.usedbook.domain.enums.SaleStatus;

public class OrderBasketDTO {

    @Data
    @NoArgsConstructor
    public static class Response{
        private Long id;
        private String writer;
        private String title;
        private int price;
        private int stock;
        private SaleStatus saleStatus;

        private int count;
    }

    @Data
    @NoArgsConstructor
    public static class basketToOrder{
        private Long postid;
        private int count;
    }
}
