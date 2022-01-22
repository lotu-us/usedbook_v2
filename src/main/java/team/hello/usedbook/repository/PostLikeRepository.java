package team.hello.usedbook.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import team.hello.usedbook.domain.PostLike;

@Mapper
@Repository
public interface PostLikeRepository {

    @Insert("insert into postlike(memberid, postid) values(#{memberId}, #{postId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void add(PostLike postLike);

    @Delete("delete from postlike where memberid=#{memberId} and postid=#{postId}")
    int remove(PostLike postLike);

    @Select("select * from postlike where memberid=#{memberId} and postid=#{postId}")
    PostLike find(Long memberId, Long postId);
}
