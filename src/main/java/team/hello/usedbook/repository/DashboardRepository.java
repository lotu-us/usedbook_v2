package team.hello.usedbook.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import team.hello.usedbook.domain.Member;
import team.hello.usedbook.domain.Post;
import team.hello.usedbook.domain.dto.CommentDTO;
import team.hello.usedbook.domain.dto.OrderDTO;
import team.hello.usedbook.domain.dto.Pagination;

import java.util.List;

@Mapper
@Repository
public interface DashboardRepository {

    @Select("select count(*) from post where writer=#{nickname}")
    int findMyPostsCount(Member loginMember);

    //DashboardMapper참고
    List<Post> findMyPosts(@Param("loginMember") Member loginMember, @Param("pagination") Pagination pagination);



    @Select("select count(*) from comment where writer=#{nickname}")
    int findMyCommentsCount(Member loginMember);

    //DashboardMapper참고
    List<CommentDTO.DashboardResponse> findMyComments(@Param("loginMember") Member loginMember, @Param("pagination") Pagination pagination);



    @Select("select count(*) from post join postlike on post.id=postlike.postid where postlike.memberid=#{id}")
    int findMyFavoritesCount(Member loginMember);

    //DashboardMapper참고
    List<Post> findMyFavorites(@Param("loginMember") Member loginMember, @Param("pagination") Pagination pagination);

    @Select("select count(*) from orders where memberid=#{id}")
    int findMyOrdersCount(Member loginMember);

    //mapper
    List<OrderDTO.OrderListItem> findMyOrders(@Param("loginMember") Member loginMember, @Param("pagination") Pagination pagination);
}
