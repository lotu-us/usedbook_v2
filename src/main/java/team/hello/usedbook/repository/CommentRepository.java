package team.hello.usedbook.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import team.hello.usedbook.domain.Comment;

import java.util.List;

@Mapper
@Repository
public interface CommentRepository {

    @Insert("insert into comment(postid, writer, content, parentid, depth, createtime) " +
            "values(#{postId}, #{writer}, #{content}, #{parentId}, #{depth}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(Comment comment);

    @Select("select * from comment where id=#{commentId}")
    Comment findById(Long commentId);

    @Select("select * from comment where postid=#{postId}")
    List<Comment> findAll(Long postId);

    @Update("update comment set content=#{content} where id=#{commentId} and postid=#{postId}")
    int update(@Param("postId") Long postId, @Param("commentId") Long commentId, @Param("content") String content);

    @Delete("delete from comment where id=#{commentId} and postid=#{postId}")
    int delete(Long postId, Long commentId);

    @Update("update comment set viewstatus='0' where id=#{commentId} and postid=#{postId}")
    int viewStatusChange(Long postId, Long commentId);

    @Select("select count(*) from comment where postid=#{postId}")
    int findByPostId(Long postId);
}
