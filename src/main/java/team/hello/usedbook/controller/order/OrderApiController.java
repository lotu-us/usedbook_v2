package team.hello.usedbook.controller.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import team.hello.usedbook.config.SessionConstants;
import team.hello.usedbook.domain.Member;
import team.hello.usedbook.domain.dto.OrderBasketDTO;
import team.hello.usedbook.domain.dto.OrderDTO;
import team.hello.usedbook.repository.OrderRepository;
import team.hello.usedbook.service.OrderBasketService;
import team.hello.usedbook.service.OrderService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/api")
public class OrderApiController {
    @Autowired private OrderService orderService;
    @Autowired private OrderBasketService orderBasketService;
    @Autowired private OrderRepository orderRepository;


    @PostMapping("/basketToOrder")
    public ResponseEntity saveBasketToOrder(@RequestBody String arr, HttpSession session){

        List<Long> postIdList = orderService.jsonArrToPostIdList(arr);

        //결제내용 우선 세션에 저장 (구매 할지 안할지 모르니까)
        session.setAttribute(SessionConstants.BAKET_TO_ORDER, postIdList);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }


    @GetMapping("/basketToOrder")
    public ResponseEntity getBasketToOrder(HttpSession session){

        List<Long> postIdList = (List) session.getAttribute(SessionConstants.BAKET_TO_ORDER);
        if(postIdList == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("장바구니에서 구매할 물건을 선택해주세요.");
        }

        List<OrderBasketDTO.Response> basketToOrder = orderService.getBasketToOrder(postIdList);
        return ResponseEntity.status(HttpStatus.OK).body(basketToOrder);
    }





    @PostMapping("/order")
    @Transactional
    public ResponseEntity saveOrders(@RequestBody OrderDTO.OrderForm orderForm, HttpSession session){

        String orderId = orderService.orderSave(orderForm, session);
        orderService.deleteOrderedPost(orderForm, session);

        return ResponseEntity.status(HttpStatus.OK).body(orderId);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity getOrderDetail(@PathVariable String orderId, HttpSession session){

        Member loginMember = (Member)session.getAttribute(SessionConstants.LOGIN_MEMBER);
        OrderDTO.OrderDetail order = orderRepository.getOrderDetail(orderId, loginMember.getId());

        return ResponseEntity.status(HttpStatus.OK).body(order);
    }


}


