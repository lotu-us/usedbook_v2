package team.hello.usedbook.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.hello.usedbook.config.SessionConstants;
import team.hello.usedbook.domain.Address;
import team.hello.usedbook.domain.Member;
import team.hello.usedbook.domain.Orders;
import team.hello.usedbook.domain.dto.OrderBasketDTO;
import team.hello.usedbook.domain.dto.OrderDTO;
import team.hello.usedbook.repository.AddressRepository;
import team.hello.usedbook.repository.OrderBasketRepository;
import team.hello.usedbook.repository.OrderRepository;

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


    public void orderSave(OrderDTO.OrderForm orderForm, HttpSession session) {

        Member loginMember = (Member) session.getAttribute(SessionConstants.LOGIN_MEMBER);

        //날짜
        String orderTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        //주문번호 만들기
        String uuid = UUID.randomUUID().toString().substring(0, 6);
        String time = orderTime.substring(2, 16)
                .replace("-", "")
                .replace(":", "")
                .replace(" ", "");
        String orderId = time + "_" + uuid;


        for (OrderDTO.OrderPosts orderPosts : orderForm.getPostList()) {
            Address address = orderForm.getAddress();
            addressRepository.save(address);

            Orders orders = new Orders(
                    orderId,
                    loginMember.getId(),
                    orderPosts.getId(),
                    orderPosts.getCount(),
                    orderTime,
                    address.getId()
            );
            orderRepository.save(orders);
        }
    }



}
