package team.hello.usedbook.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;
import team.hello.usedbook.domain.Post;

@Mapper
@Repository
public interface PostRepository {

    @Insert("insert into post(writer, title, content, price, stock, category, salestatus, createtime) " +
            "values(#{writer}, #{title}, #{content}, #{price}, #{stock}, #{category}, #{saleStatus}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(Post post);
}
