package team.hello.usedbook.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import team.hello.usedbook.domain.dto.PostDTO;

import java.util.List;

@Mapper
@Repository
public interface IndexRepository {

    @Select("select * from post where category=#{lowerCategory} order by createtime desc limit #{count} offset 0")
    @ResultMap("team.hello.usedbook.repository.PostRepository.postAndFile")
    List<PostDTO.Response> findAllForIndex(String lowerCategory, int count);

}
