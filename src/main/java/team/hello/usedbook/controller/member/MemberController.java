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

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class MemberController {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;


//    @Autowired RegisterValidator registerValidator;
//    @InitBinder
//    protected void initBinder(WebDataBinder binder) {
//        binder.addValidators(registerValidator);
//    }

    @GetMapping("/login")
    public String loginForm(HttpSession session){
        Member member = (Member) session.getAttribute(SessionConstants.LOGIN_MEMBER);
        if(member != null){
            return "index";
        }
        return "member/login";
    }

    @PostMapping("/login")
    public String loginOk(@ModelAttribute MemberDTO.LoginForm loginForm, HttpSession session, @RequestParam(defaultValue = "/") String redirectURL){
        //api로 아무리 검증한다쳐도 이렇게 무방비해도되나?? 그렇다고 api컨트롤러를 다시 호출해..? 추후 개선하자
        
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
    public String registerSave(@Validated @ModelAttribute MemberDTO.RegisterForm registerForm){
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
        Member byEmail = memberRepository.findByEmail(findForm.getEmail());

        String tempPassword = memberService.createTempPasswordAndSendMail(byEmail);
        memberRepository.updatePassword(byEmail.getId(), tempPassword);

        return "member/findPasswordEmailSend";
    }


    //=========================================================================================================================================


}
