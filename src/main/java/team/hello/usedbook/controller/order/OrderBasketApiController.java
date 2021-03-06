package team.hello.usedbook.controller.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import team.hello.usedbook.config.SessionConstants;
import team.hello.usedbook.domain.Member;
import team.hello.usedbook.domain.OrderBasket;
import team.hello.usedbook.domain.dto.OrderBasketDTO;
import team.hello.usedbook.repository.OrderBasketRepository;
import team.hello.usedbook.service.OrderBasketService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/api")
public class OrderBasketApiController {
    @Autowired  private OrderBasketService orderBasketService;
    @Autowired  private OrderBasketRepository orderBasketRepository;

    @PostMapping("/order/basket/{postId}/{count}")
    public ResponseEntity addOrderBasket(@PathVariable Long postId, @PathVariable int count, HttpSession session) {

        Member loginMember = (Member) session.getAttribute(SessionConstants.LOGIN_MEMBER);

        String errorMessage = orderBasketService.addCheck(loginMember, postId, count);
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

}


