package team.hello.usedbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.hello.usedbook.config.SessionConstants;
import team.hello.usedbook.domain.Member;
import team.hello.usedbook.domain.Post;
import team.hello.usedbook.domain.dto.CommentDTO;
import team.hello.usedbook.domain.dto.Pagination;
import team.hello.usedbook.domain.dto.PostDTO;
import team.hello.usedbook.repository.DashboardRepository;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static team.hello.usedbook.domain.dto.PostDTO.Response.ListPostToListDto;

@Service
public class DashboardService {
    @Autowired private DashboardRepository dashboardRepository;

    public Map<String, Object> findMyPosts(HttpSession session, Pagination pagination) {
        Member loginMember = (Member) session.getAttribute(SessionConstants.LOGIN_MEMBER);

        pagination.setCategory(null);
        int count = dashboardRepository.findMyPostsCount(loginMember);

        pagination.init(count);
        List<Post> posts = dashboardRepository.findMyPosts(loginMember, pagination);

        Map<String, Object> result = new HashMap<>();

        List<PostDTO.Response> responses = ListPostToListDto(posts);
        result.put("posts", responses);
        result.put("pagination", pagination);

        return result;
    }


    public Map<String, Object> findMyComments(HttpSession session, Pagination pagination) {
        Member member = (Member) session.getAttribute(SessionConstants.LOGIN_MEMBER);

        pagination.setCategory(null);
        int allForDashboardCount = dashboardRepository.findMyCommentsCount(member);

        pagination.init(allForDashboardCount);
        List<CommentDTO.DashboardResponse> comments = dashboardRepository.findMyComments(member, pagination);

        Map<String, Object> result = new HashMap<>();
        result.put("comments", comments);
        result.put("pagination", pagination);

        return result;
    }

    public Map<String, Object> findMyFavorites(HttpSession session, Pagination pagination) {
        Member loginMember = (Member) session.getAttribute(SessionConstants.LOGIN_MEMBER);

        pagination.setCategory(null);
        int count = dashboardRepository.findMyFavoritesCount(loginMember);

        pagination.init(count);
        List<Post> posts = dashboardRepository.findMyFavorites(loginMember, pagination);

        Map<String, Object> result = new HashMap<>();

        List<PostDTO.Response> responses = ListPostToListDto(posts);
        result.put("posts", responses);
        result.put("pagination", pagination);

        return result;
    }
}
