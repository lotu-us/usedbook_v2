package team.hello.usedbook.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;
import team.hello.usedbook.domain.Orders;

import java.util.List;

@Mapper
@Repository
public interface OrderRepository {

    @Insert("insert into orders(orderid, memberid, postid, count, status, payment, ordertime, addressid) " +
            "values(#{orderId}, #{memberId}, #{postId}, #{count}, #{orderStatus}, #{payment}, #{orderTime}, #{addressId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(Orders orders);

    //@Select("select ")
    List<Orders> findById(Long id, Long orderId);
}
