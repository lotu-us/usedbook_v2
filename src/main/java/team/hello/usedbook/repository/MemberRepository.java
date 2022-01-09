package team.hello.usedbook.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import team.hello.usedbook.domain.Member;

import java.util.List;

@Mapper
@Repository
public interface MemberRepository {

    @Select("select count(*) from member")
    public List<Member> findAll();

    public List<Member> findAllXml();

    @Select("select * from member where email=#{email}")
    public Member findByEmail(String email);

    @Select("select * from member where nickname=#{nickname}")
    public Member findByNickName(String nickname);

    @Insert("insert into member(email, nickname, password) values(#{email}, #{nickname}, #{password})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(Member member);
}
