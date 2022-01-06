package team.hello.usedbook.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import team.hello.usedbook.domain.Member;

import java.util.List;

@Mapper
@Repository
public interface MemberMapper {

    @Select("select count(*) from member")
    public List<Member> findAll();

    public List<Member> findAllXml();
}
