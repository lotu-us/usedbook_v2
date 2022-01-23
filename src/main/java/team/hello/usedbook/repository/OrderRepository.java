package team.hello.usedbook.repository;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import team.hello.usedbook.domain.Order;

@Mapper
@Repository
public interface OrderRepository {

//    @Insert("")
//    @Options();
    void addOrder(Order order);
}
