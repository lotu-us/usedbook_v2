package team.hello.usedbook.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;
import team.hello.usedbook.domain.OrderPost;

@Mapper
@Repository
public interface OrderPostRepository {

    @Insert("insert into orderpost(orderid, postid, count) values(#{orderId}, #{postId}, #{count})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(OrderPost orderPost);
}
