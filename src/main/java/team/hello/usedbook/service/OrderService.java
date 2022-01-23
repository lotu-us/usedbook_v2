package team.hello.usedbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.hello.usedbook.domain.Member;
import team.hello.usedbook.domain.OrderBasket;
import team.hello.usedbook.domain.Post;
import team.hello.usedbook.repository.OrderBasketRepository;
import team.hello.usedbook.repository.PostRepository;

@Service
public class OrderService {
    @Autowired private OrderBasketRepository orderBasketRepository;
    @Autowired private PostRepository postRepository;

    public String addCheck(Member loginMember, Long postId, int count) {

        if(loginMember == null){
            return "장바구니는 회원만 이용할 수 있습니다.";
        }

        Post post = postRepository.findById(postId);
        if(post == null){
            return "해당 게시글이 존재하지 않습니다.";
        }

        if(post.getStock() < count){
            return "상품의 수량을 초과하여 장바구니에 담을 수 없습니다.";
        }

        OrderBasket orderBasket = orderBasketRepository.findOrderBasket(loginMember.getId(), postId);
        if(orderBasket != null){
            return "이미 장바구니에 담긴 상품입니다. 장바구니 페이지에서 수량을 변경해주세요.";
        }

        return null;
    }

}
