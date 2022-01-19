package team.hello.usedbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import team.hello.usedbook.config.SessionConstants;
import team.hello.usedbook.domain.Comment;
import team.hello.usedbook.domain.Member;
import team.hello.usedbook.domain.dto.CommentDTO;
import team.hello.usedbook.repository.CommentRepository;
import team.hello.usedbook.utils.ValidResultList;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class CommentService {
    @Autowired private CommentRepository commentRepository;

    public List<ValidResultList.ValidResult> commentSaveCheck(CommentDTO commentForm, BindingResult bindingResult) {
        List<ValidResultList.ValidResult> validResults = new ValidResultList(bindingResult).getList();
        return validResults;
    }

    public Long commentSave(Long postId, CommentDTO commentForm, HttpSession session) {
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


}
