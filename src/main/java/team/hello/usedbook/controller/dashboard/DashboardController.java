package team.hello.usedbook.controller.dashboard;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import team.hello.usedbook.config.SessionConstants;
import team.hello.usedbook.domain.Member;

import javax.servlet.http.HttpSession;

@Controller
public class DashboardController {

    @GetMapping("/dashboard/myInfo")
    public String myInfo(HttpSession session, Model model){
        Member loginMember = (Member) session.getAttribute(SessionConstants.LOGIN_MEMBER);
        model.addAttribute("loginMember", loginMember);
        return "dashboard/myInfo";
    }

    @GetMapping("/dashboard/myPosts")
    public String myPosts(){
        return "dashboard/myPosts";
    }

    @GetMapping("/dashboard/myComments")
    public String myComments(){
        return "dashboard/myComments";
    }

    @GetMapping("/dashboard/myFavorites")
    public String myFavorites(){
        return "dashboard/myFavorites";
    }
}
