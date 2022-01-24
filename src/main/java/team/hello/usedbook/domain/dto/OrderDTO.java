package team.hello.usedbook.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import team.hello.usedbook.domain.Address;
import team.hello.usedbook.domain.enums.Payment;

import java.util.List;

public class OrderDTO {

    @Data
    @NoArgsConstructor
    public static class OrderPosts{
        private Long id;
        private int count;
    }

    @Data
    @NoArgsConstructor
    public static class OrderForm{
        private List<OrderPosts> postList;
        private Payment payment;
        private Address address;
    }

//    @Data
//    @NoArgsConstructor
//    public static class Response{
//        private
//    }

}
