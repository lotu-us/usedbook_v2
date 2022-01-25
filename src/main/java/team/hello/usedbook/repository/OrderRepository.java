package team.hello.usedbook.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import team.hello.usedbook.domain.Orders;
import team.hello.usedbook.domain.dto.OrderDTO;

@Mapper
@Repository
public interface OrderRepository {

    @Insert("insert into orders(orderid, memberid, status, payment, ordertime) " +
            "values(#{orderId}, #{memberId}, #{orderStatus}, #{payment}, #{orderTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(Orders orders);


    //mapper 참고
    OrderDTO.OrderDetail getOrderDetail(@Param("orderId") String orderId, @Param("memberId") Long memberId);


}
