package team.hello.usedbook.controller.post;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team.hello.usedbook.domain.dto.PostDTO;
import team.hello.usedbook.repository.PostRepository;
import team.hello.usedbook.service.PostService;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class PostController {

    @Autowired private PostService postService;
    @Autowired private PostRepository postRepository;

    @GetMapping("/post")
    public String writeForm(){
        return "post/write";
    }

    @PostMapping("/post")
    public String writeSave(@Validated @ModelAttribute PostDTO.EditForm editForm, BindingResult bindingResult, HttpSession session){
        if(bindingResult.hasErrors()){
            return "post/write";
        }
        return "redirect:/posts";
    }

    @GetMapping("/post/update/{postid}")
    public String updateForm(@PathVariable Integer postid){
        return "post/update";
    }

    @PostMapping("/post/update/{postid}")
    public String updateSave(@PathVariable Integer postid){
        return "redirect:/post/detail/"+postid;
    }

    @GetMapping("/post/detail/{postid}")
    public String detailForm(@PathVariable Integer postid){
        return "post/detail";
    }


    @GetMapping({"/posts", "/posts/{category}"})
    public String list(@PathVariable(required = false) String category){
        return "post/list";
    }
}
