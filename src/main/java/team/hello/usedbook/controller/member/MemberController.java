package team.hello.usedbook.controller.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import team.hello.usedbook.config.SessionConstants;
import team.hello.usedbook.domain.Member;
import team.hello.usedbook.domain.dto.MemberDTO;
import team.hello.usedbook.repository.MemberRepository;
import team.hello.usedbook.service.MemberService;
import team.hello.usedbook.utils.ValidResultList;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Controller
public class MemberController {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @GetMapping("/login")
    public String loginForm(HttpSession session){
        Member member = (Member) session.getAttribute(SessionConstants.LOGIN_MEMBER);
        if(member != null){ //세션 있을땐 로그인 화면 접속 못하게
            return "redirect:/";
        }
        return "member/login";
    }

    @PostMapping("/login")
    public String loginOk(@ModelAttribute MemberDTO.LoginForm loginForm, BindingResult bindingResult, HttpSession session, @RequestParam(defaultValue = "/") String redirectURL){

        List<ValidResultList.ValidResult> validResults = memberService.loginCheck(loginForm, bindingResult);
        if(!validResults.isEmpty()){
            return "member/login";
        }

        //session처리
        Member byEmail = memberRepository.findByEmail(loginForm.getEmail());
        session.setAttribute(SessionConstants.LOGIN_MEMBER, byEmail);

        return "redirect:"+redirectURL;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }

    //=========================================================================================================================================
    @GetMapping("/register")
    public String registerForm(){
        return "member/register";
    }

    @PostMapping("/register")
    public String registerSave(@Validated @ModelAttribute MemberDTO.RegisterForm registerForm, BindingResult bindingResult){
        List<ValidResultList.ValidResult> validResults = memberService.registerCheck(registerForm, bindingResult);
        if(!validResults.isEmpty()){
            return "member/register";
        }

        memberService.registerSave(registerForm);
        return "redirect:/registerOk";
    }

    @GetMapping("/registerOk")
    public String registerOk(){
        return "member/registerOk";
    }

    //=========================================================================================================================================

    @GetMapping("/findPassword")
    public String findPasswordForm(){
        return "member/findPassword";
    }

    @PostMapping("/findPassword")
    public String findPassword(@ModelAttribute MemberDTO.FindForm findForm, BindingResult bindingResult){
        List<ValidResultList.ValidResult> validResults = memberService.findPasswordCheck(findForm, bindingResult);
        if(!validResults.isEmpty()){
            return "member/findPassword";
        }

        Member byEmail = memberRepository.findByEmail(findForm.getEmail());

        String tempPassword = memberService.createTempPasswordAndSendMail(byEmail);
        memberRepository.updatePassword(byEmail.getId(), tempPassword);

        return "member/findPasswordEmailSend";
    }

    //=========================================================================================================================================


}
