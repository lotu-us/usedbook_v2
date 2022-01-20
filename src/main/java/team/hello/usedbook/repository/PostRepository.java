package team.hello.usedbook.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import team.hello.usedbook.domain.Member;
import team.hello.usedbook.domain.Post;
import team.hello.usedbook.domain.dto.Pagination;

import java.util.List;

@Mapper
@Repository
public interface PostRepository {
    //$는 값만 반환, #은 ""을 포함하여 반환

    @Insert("insert into post(writer, title, content, price, stock, category, salestatus, createtime) " +
            "values(#{writer}, #{title}, #{content}, #{price}, #{stock}, #{category}, #{saleStatus}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(Post post);

    //PostMapper.xml 파일 참고
    List<Post> findAll(Pagination pagination);

    //PostMapper.xml 파일 참고
    int findAllCount(Pagination pagination);

    @Update("update post set title=#{post.title}, content=#{post.content}, price=#{post.price}, stock=#{post.stock}, category=#{post.category}, salestatus=#{post.saleStatus} " +
            "where id=#{postId}")
    int update(@Param("postId") Long postId, @Param("post") Post post);

    @Select("select * from post where id=#{postId}")
    Post findById(Long postId);

    @Delete("delete from post where id=#{postId}")
    int deleteById(Long postId);

    @Select("select * from post where category=#{lowerCategory} order by createtime desc limit #{count} offset 0")
    List<Post> findAllForIndex(String lowerCategory, int count);

    //postmapper참고
    List<Post> findAllForDashboard(@Param("loginMember") Member loginMember, @Param("pagination") Pagination pagination);

    @Select("select count(*) from post where writer=#{nickname}")
    int findAllForDashboardCount(Member loginMember);

    @Update("update post set commentcount=(commentcount+1) where id=#{postId}")
    void addCommentCount(Long postId);

    @Update("update post set viewcount=(viewcount+1) where id=#{postId}")
    void addViewCount(Long postId);
}
