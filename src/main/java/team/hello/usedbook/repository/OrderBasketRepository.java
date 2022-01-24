package team.hello.usedbook.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import team.hello.usedbook.domain.OrderBasket;
import team.hello.usedbook.domain.dto.OrderBasketDTO;

import java.util.List;

@Mapper
@Repository
public interface OrderBasketRepository {

    @Insert("insert into orderbasket(memberid, postid, count) values(#{memberId}, #{postId}, #{count})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void addOrderBasket(OrderBasket orderBasket);

    @Select("select * from orderbasket where memberid=#{memberId} and postid=#{postId}")
    OrderBasket findOrderBasket(Long memberId, Long postId);

    @Select("SELECT post.id, post.writer, post.title, post.price, post.stock, post.salestatus, orderbasket.count " +
            "FROM orderbasket JOIN post ON orderbasket.postid = post.id " +
            "WHERE post.id=#{postId}")
    OrderBasketDTO.Response findByPostId(Long postId);

    @Select("SELECT post.id, post.writer, post.title, post.price, post.stock, post.salestatus, orderbasket.count " +
            "FROM orderbasket JOIN post ON orderbasket.postid = post.id " +
            "WHERE memberid=#{memberId}")
    List<OrderBasketDTO.Response> findAll(Long memberId);



    @Update("update orderbasket set count=#{count} where memberid=#{memberId} and postid=#{postId}")
    int updateBasket(Long memberId, Long postId, int count);

    @Delete("delete from orderbasket where memberid=#{memberId} and postid=#{postId}")
    int deleteBasket(Long memberId, Long postId);


}
