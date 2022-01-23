package team.hello.usedbook.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;
import team.hello.usedbook.domain.Orders;

@Mapper
@Repository
public interface OrderRepository {

    @Insert("insert into orders(orderid, memberid, postid, count, status, payment, ordertime) " +
            "values(#{orderId}, #{memberId}, #{postId}, #{count}, #{orderStatus}, #{payment}, #{orderTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void addOrder(Orders orders);
}
