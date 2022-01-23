package team.hello.usedbook.controller.order;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import team.hello.usedbook.config.SessionConstants;
import team.hello.usedbook.domain.Member;
import team.hello.usedbook.domain.Order;
import team.hello.usedbook.domain.OrderBasket;
import team.hello.usedbook.domain.dto.OrderBasketDTO;
import team.hello.usedbook.domain.enums.OrderStatus;
import team.hello.usedbook.repository.OrderBasketRepository;
import team.hello.usedbook.repository.OrderRepository;
import team.hello.usedbook.service.OrderService;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api")
public class OrderApiController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderBasketRepository orderBasketRepository;

    @PostMapping("/order/basket/{postId}/{count}")
    public ResponseEntity addOrderBasket(@PathVariable Long postId, @PathVariable int count, HttpSession session) {

        Member loginMember = (Member) session.getAttribute(SessionConstants.LOGIN_MEMBER);

        String errorMessage = orderService.addCheck(loginMember, postId, count);
        if (errorMessage != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }

        OrderBasket orderBasket = new OrderBasket(loginMember.getId(), postId, count);
        orderBasketRepository.addOrderBasket(orderBasket);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/order/basket")
    public ResponseEntity getOrderBasket(HttpSession session) {

        Member loginMember = (Member) session.getAttribute(SessionConstants.LOGIN_MEMBER);
        if (loginMember == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("장바구니를 이용하려면 로그인해주세요.");
        }

        List<OrderBasketDTO.Response> all = orderBasketRepository.findAll(loginMember.getId());

        return ResponseEntity.status(HttpStatus.OK).body(all);
    }


    @PutMapping("/order/basket/{postId}/{count}")
    public ResponseEntity updateOrderBasket(@PathVariable Long postId, @PathVariable int count, HttpSession session) {

        Member loginMember = (Member) session.getAttribute(SessionConstants.LOGIN_MEMBER);
        if (loginMember == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("장바구니를 이용하려면 로그인해주세요.");
        }

        int updated = orderBasketRepository.updateBasket(loginMember.getId(), postId, count);

        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }


    @DeleteMapping("/order/basket/{postId}")
    public ResponseEntity deleteOrderBasket(@PathVariable Long postId, HttpSession session) {

        Member loginMember = (Member) session.getAttribute(SessionConstants.LOGIN_MEMBER);
        if (loginMember == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("장바구니를 이용하려면 로그인해주세요.");
        }

        int updated = orderBasketRepository.deleteBasket(loginMember.getId(), postId);

        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }





    @PostMapping("/order")
    public ResponseEntity order(@RequestBody String arr, HttpSession session){

        Member loginMember = (Member) session.getAttribute(SessionConstants.LOGIN_MEMBER);

        ObjectMapper mapper = new ObjectMapper();

        List<OrderBasketDTO.basketToOrder> basketList = new ArrayList();
        try{
            List<Object> list = mapper.readValue(arr, new TypeReference<List>() {});

            for (int i = 0; i < list.size(); i++) {
                String json = mapper.writeValueAsString(list.get(i));
                OrderBasketDTO.basketToOrder basketToOrder = mapper.readValue(json, OrderBasketDTO.basketToOrder.class);
                basketList.add(basketToOrder);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //날짜
        String orderTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        //주문번호 만들기
        String uuid = UUID.randomUUID().toString().substring(0, 6);
        String time = orderTime.substring(2, 16)
                .replace("-", "")
                .replace(":", "")
                .replace(" ", "");
        String orderId = time + "_" + uuid;


        for (OrderBasketDTO.basketToOrder basket : basketList) {


            Order order = new Order(
                    orderId,
                    loginMember.getId(),
                    basket.getPostid(),
                    basket.getCount(),
                    OrderStatus.READY,
                    orderTime
            );

            orderRepository.addOrder(order);
        }


        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}


