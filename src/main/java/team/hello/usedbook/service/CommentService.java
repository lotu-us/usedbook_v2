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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentService {
    @Autowired private CommentRepository commentRepository;

    public Map<String, Object> commentList(Long postId, HttpSession session) {
        List<CommentDTO.Response> commentList = commentRepository.findAll(postId);
        Member loginMember = (Member) session.getAttribute(SessionConstants.LOGIN_MEMBER);

        for (CommentDTO.Response comment : commentList) {
            if(comment.getViewStatus() == 0){
                comment.deletedCommentHidePrivacy();
            }

            //조회하는 자가 회원이고 댓글 작성자일떄
            if(loginMember != null && loginMember.getNickname().equals(comment.getWriter())){
                comment.setCommentMenu(true);   //수정 삭제메뉴 보이게
            }
        }

        //조회하는 자가 회원인 경우에만 댓글 답글 가능
        boolean commentWriteStatus = false;
        if(loginMember != null){
            commentWriteStatus = true;
        }

        Map<String, Object> result = new HashMap<>();
        result.put("commentList", commentList);
        result.put("commentWriteStatus", commentWriteStatus);
        return result;
    }

    public List<ValidResultList.ValidResult> commentSaveCheck(CommentDTO.EditForm commentForm, BindingResult bindingResult) {
        List<ValidResultList.ValidResult> validResults = new ValidResultList(bindingResult).getList();
        return validResults;
    }

    public Long commentSave(Long postId, CommentDTO.EditForm commentForm, Member loginMember) {
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
