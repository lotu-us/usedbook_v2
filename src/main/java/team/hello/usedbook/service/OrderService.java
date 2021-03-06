package team.hello.usedbook.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.hello.usedbook.config.SessionConstants;
import team.hello.usedbook.domain.Address;
import team.hello.usedbook.domain.Member;
import team.hello.usedbook.domain.OrderPost;
import team.hello.usedbook.domain.Orders;
import team.hello.usedbook.domain.dto.OrderBasketDTO;
import team.hello.usedbook.domain.dto.OrderDTO;
import team.hello.usedbook.domain.enums.OrderStatus;
import team.hello.usedbook.domain.enums.SaleStatus;
import team.hello.usedbook.repository.*;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderBasketRepository orderBasketRepository;
    @Autowired private AddressRepository addressRepository;
    @Autowired private OrderPostRepository orderPostRepository;
    @Autowired private PostRepository postRepository;

    public List<Long> jsonArrToPostIdList(String arr) {

        ObjectMapper mapper = new ObjectMapper();
        List<Long> list = new ArrayList<>();
        try {
             list = mapper.readValue(arr, new TypeReference<List<Long>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return list;
    }


    public List<OrderBasketDTO.Response> getBasketToOrder(List<Long> postIdList) {

        List<OrderBasketDTO.Response> basketList = new ArrayList<>();
        for (Long postid : postIdList) {
            OrderBasketDTO.Response basket = orderBasketRepository.findByPostId(postid);
            basketList.add(basket);
        }

        return basketList;
    }

    @Transactional
    public String orderSave(OrderDTO.OrderForm orderForm, HttpSession session) {

        Member loginMember = (Member) session.getAttribute(SessionConstants.LOGIN_MEMBER);

        //??????
        String orderTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        //???????????? ?????????
        String uuid = UUID.randomUUID().toString().substring(0, 6);
        String time = orderTime.substring(2, 16)
                .replace("-", "")
                .replace(":", "")
                .replace(" ", "");
        String orderId = time + uuid;

        Orders orders = new Orders(
                loginMember.getId(),
                orderId,
                OrderStatus.COMPLETE,
                orderForm.getPayment(),
                orderTime
        );
        orderRepository.save(orders);


        Address address = orderForm.getAddress();
        address.setOrderId(orderId);
        addressRepository.save(address);


        for (OrderPost orderPost : orderForm.getPostList()) {
            orderPost.setOrderId(orderId);
            orderPostRepository.save(orderPost);
        }

        return orderId;
    }


    public void deletePostInOrderBasket(OrderDTO.OrderForm orderForm, HttpSession session) {
        Member loginMember = (Member) session.getAttribute(SessionConstants.LOGIN_MEMBER);

        for (OrderPost orderPost : orderForm.getPostList()) {
            orderBasketRepository.deleteBasket(loginMember.getId(), orderPost.getPostId());
        }
    }

    public void minusPostStock(OrderDTO.OrderForm orderForm) {
        for (OrderPost orderPost : orderForm.getPostList()) {
            postRepository.minusPostStock(orderPost.getPostId(), orderPost.getCount(), SaleStatus.COMPLETE.toString());
        }
    }
}
