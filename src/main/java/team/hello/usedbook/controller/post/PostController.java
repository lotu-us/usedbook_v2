package team.hello.usedbook.controller.post;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PostController {

    @GetMapping("/post/write")
    public String writePost(){

        return "post/write";
    }
}
