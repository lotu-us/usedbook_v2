package team.hello.usedbook.controller.order;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderController {

    @GetMapping("/order/basket")
    public String orderbasket(){
        return "order/orderbasket";
    }

    @GetMapping("/order")
    public String order(){
        return "order/order";
    }
}
