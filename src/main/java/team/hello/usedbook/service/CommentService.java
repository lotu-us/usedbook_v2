package team.hello.usedbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import team.hello.usedbook.config.SessionConstants;
import team.hello.usedbook.domain.Comment;
import team.hello.usedbook.domain.Member;
import team.hello.usedbook.domain.dto.CommentDTO;
import team.hello.usedbook.domain.dto.Pagination;
import team.hello.usedbook.repository.CommentRepository;
import team.hello.usedbook.utils.ValidResultList;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentService {
    @Autowired private CommentRepository commentRepository;

    public List<ValidResultList.ValidResult> commentSaveCheck(CommentDTO.EditForm commentForm, BindingResult bindingResult) {
        List<ValidResultList.ValidResult> validResults = new ValidResultList(bindingResult).getList();
        return validResults;
    }

    public Long commentSave(Long postId, CommentDTO.EditForm commentForm, HttpSession session) {
        Member member = (Member) session.getAttribute(SessionConstants.LOGIN_MEMBER);
        String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        Comment comment = new Comment(
                postId,
                //member.getNickname(),
                "11",
                commentForm.getContent(),
                commentForm.getParentId(),
                commentForm.getDepth(),
                createTime
        );

        commentRepository.save(comment);
        return comment.getId();
    }


    public Map<String, Object> dashboardGetMyComments(HttpSession session, Pagination pagination) {
        Member member = (Member) session.getAttribute(SessionConstants.LOGIN_MEMBER);

        pagination.setCategory(null);
        int allForDashboardCount = commentRepository.findAllForDashboardCount(member);

        pagination.init(allForDashboardCount);
        List<CommentDTO.DashboardResponse> comments = commentRepository.findAllForDashboard(member, pagination);

        Map<String, Object> result = new HashMap<>();
        result.put("comments", comments);
        result.put("pagination", pagination);

        return result;
    }
}
