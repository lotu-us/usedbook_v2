package team.hello.usedbook.controller.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import team.hello.usedbook.repository.OrderRepository;
import team.hello.usedbook.service.OrderService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/api")
public class OrderApiController {
    @Autowired private OrderService orderService;
    @Autowired private OrderRepository orderRepository;


    @PostMapping("/order")
    public ResponseEntity order(@RequestBody String arr, HttpSession session){

        orderService.addOrders(arr, session);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}


