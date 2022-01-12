package team.hello.usedbook.controller.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import team.hello.usedbook.domain.dto.PostDTO;
import team.hello.usedbook.repository.PostRepository;
import team.hello.usedbook.service.PostService;
import team.hello.usedbook.utils.ValidResultList;

import javax.servlet.http.HttpSession;

@Controller
public class PostController {

    @Autowired private PostService postService;
    @Autowired private PostRepository postRepository;

    @GetMapping("/post")
    public String writeForm(){
        return "post/write";
    }

    @PostMapping("/post")
    @ResponseBody
    public Object writeSave(@Validated @ModelAttribute PostDTO.EditForm editForm, BindingResult bindingResult, HttpSession session){
        if(bindingResult.hasErrors()){
            return new ValidResultList(bindingResult).getList();
        }

        Long postId = postService.postSave(session, editForm);
        postService.postFileSave(postId, editForm);

        return null;
    }

    @GetMapping("/posts")
    public String list(){
        return "post/list";
    }
}
