package team.hello.usedbook.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import team.hello.usedbook.domain.Address;
import team.hello.usedbook.domain.OrderPost;
import team.hello.usedbook.domain.enums.OrderStatus;
import team.hello.usedbook.domain.enums.Payment;

import java.util.List;

public class OrderDTO {

    @Data
    @NoArgsConstructor
    public static class OrderForm{
        private List<OrderPost> postList;
        private Payment payment;
        private Address address;
    }

    @Data
    @NoArgsConstructor
    public static class OrderDetail {
        private Long id;
        private String orderId;
        private Long memberId;
        private OrderStatus status;
        private Payment payment;
        private String orderTime;

        private Address address;
        private List<OrderPostDTO.Response> orderPostList;

        public String getOrderTime() {
            if (orderTime.length() == 21) {
                return orderTime.substring(2, orderTime.length() - 5);
                //DB에 저장될때도 substring되어서 저장되어버림.. 뷰에서 가져올때만 처리되도록
            }
            return orderTime;
        }
    }

    @Data
    @NoArgsConstructor
    public static class OrderListItem {
        private String orderId;
        private List<OrderPostDTO.Response> orderPostList;
        private String orderTime;

        public String getOrderTime() {
            if (orderTime.length() == 21) {
                return orderTime.substring(2, orderTime.length() - 5);
                //DB에 저장될때도 substring되어서 저장되어버림.. 뷰에서 가져올때만 처리되도록
            }
            return orderTime;
        }
    }

}
