package team.hello.usedbook.controller.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team.hello.usedbook.domain.Comment;
import team.hello.usedbook.domain.dto.CommentDTO;
import team.hello.usedbook.domain.dto.Pagination;
import team.hello.usedbook.repository.CommentRepository;
import team.hello.usedbook.service.CommentService;
import team.hello.usedbook.service.PostService;
import team.hello.usedbook.utils.ValidResultList;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class CommentApiController {
    @Autowired private CommentService commentService;
    @Autowired private CommentRepository commentRepository;
    @Autowired private PostService postService;

    @GetMapping("/comment/{postId}")
    public ResponseEntity commentGet(@PathVariable Long postId){
        List<Comment> commentList = commentRepository.findAll(postId);
        return ResponseEntity.status(HttpStatus.OK).body(commentList);
    }

    @PostMapping("/comment/{postId}")
    public ResponseEntity commentSave(@PathVariable Long postId, @Validated @RequestBody CommentDTO.EditForm commentForm, BindingResult bindingResult, HttpSession session){
        List<ValidResultList.ValidResult> validResults = commentService.commentSaveCheck(commentForm, bindingResult);
        if(bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validResults);
        }

        Long commentId = commentService.commentSave(postId, commentForm, session);
        Comment comment = commentRepository.findById(commentId);

        //추가된 상태
        postService.addCommentCount(postId);

        return ResponseEntity.status(HttpStatus.OK).body(comment);
    }

    @PutMapping("/comment/{postId}/{commentId}")
    public ResponseEntity commentUpdate(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody Map<String, String> map){
        System.out.println(map);
        if(map.get("content").isBlank()){
            String err = "{\"field\" : \"content\", \"message\" : \"내용을 입력해주세요\"}";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
        }

        int update = commentRepository.update(postId, commentId, map.get("content"));
        return ResponseEntity.status(HttpStatus.OK).body(update);
    }

//    @DeleteMapping("/comment/{postId}/{commentId}")
//    public ResponseEntity commentDelete(@PathVariable Long postId, @PathVariable Long commentId){
//
//        int delete = commentRepository.delete(postId, commentId);
//        return ResponseEntity.status(HttpStatus.OK).body(delete);
//    }

    @DeleteMapping("/comment/{postId}/{commentId}")
    public ResponseEntity commentDelete(@PathVariable Long postId, @PathVariable Long commentId){

        int delete = commentRepository.viewStatusChange(postId, commentId);
        return ResponseEntity.status(HttpStatus.OK).body(delete);
    }

    @GetMapping("/dashboard/myComments")
    public ResponseEntity dashboardGetMyComments(HttpSession session, @ModelAttribute Pagination pagination){

        Map<String, Object> result = commentService.dashboardGetMyComments(session, pagination);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
