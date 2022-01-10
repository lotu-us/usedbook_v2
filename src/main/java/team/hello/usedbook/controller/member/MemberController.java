package team.hello.usedbook.controller.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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


//    @Autowired RegisterValidator registerValidator;
//    @InitBinder
//    protected void initBinder(WebDataBinder binder) {
//        binder.addValidators(registerValidator);
//    }

    @GetMapping("/login")
    public String loginForm(){
        return "member/login";
    }

    @PostMapping("/login")
    public String loginOk(@Validated @ModelAttribute MemberDTO.LoginForm loginForm, BindingResult bindingResult, HttpSession session){
        List list = loginCheck(loginForm, bindingResult);
        if(list != null){
            return "member/login";
        }
        //session처리
        Member byEmail = memberRepository.findByEmail(loginForm.getEmail());
        session.setAttribute(SessionConstants.LOGIN_MEMBER, byEmail);

        return "redirect:/";
    }

    @PostMapping("/loginCheck")
    @ResponseBody
    public List loginCheck(@Validated @ModelAttribute MemberDTO.LoginForm loginForm, BindingResult bindingResult){
        Member byEmail = memberRepository.findByEmail(loginForm.getEmail());
        if(byEmail == null){
            bindingResult.rejectValue("email", "notExist", "존재하지 않는 이메일입니다.");
        }else{
            boolean password = byEmail.getPassword().equals(loginForm.getPassword());
            if(!password){
                bindingResult.rejectValue("password", "notEqual", "비밀번호를 확인해주세요.");
            }
        }

        if(bindingResult.hasErrors()){
            return new ValidResultList(bindingResult).getList();
        }
        return null;
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
        List list = registerCheck(registerForm, bindingResult);
        if(list != null){
            return "member/register";
        }

        memberService.registerSave(registerForm);
        return "redirect:/registerOk";
    }

    @PostMapping("/registerCheck")
    @ResponseBody
    public List registerCheck(@Validated @ModelAttribute MemberDTO.RegisterForm registerForm, BindingResult bindingResult){
        Member byEmail = memberRepository.findByEmail(registerForm.getEmail());
        if(byEmail != null){
            bindingResult.rejectValue("email", "duplicate", "이미 존재하는 이메일입니다.");
        }

        Member byNickName = memberRepository.findByNickName(registerForm.getNickname());
        if(byNickName != null){
            bindingResult.rejectValue("nickname", "duplicate", "이미 존재하는 닉네임입니다.");
        }

        if(bindingResult.hasErrors()){
            return new ValidResultList(bindingResult).getList();
        }
        return null;
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
        List list = findPasswordCheck(findForm, bindingResult);
        if(list != null){
            return "member/findPassword";
        }

        Member byEmail = memberRepository.findByEmail(findForm.getEmail());
        String tempPassword = memberService.createTempPasswordAndSendMail(byEmail);
        memberRepository.updatePassword(byEmail.getId(), tempPassword);

        return "member/findPasswordEmailSend";
    }

    @PostMapping("/findPasswordCheck")
    @ResponseBody
    public List findPasswordCheck(@ModelAttribute MemberDTO.FindForm findForm, BindingResult bindingResult){
        Member byEmail = memberRepository.findByEmail(findForm.getEmail());
        if(byEmail == null){
            bindingResult.rejectValue("email", "notExist", "존재하지 않는 이메일입니다.");
        }else{
            boolean nickname = byEmail.getNickname().equals(findForm.getNickname());
            if(!nickname){
                bindingResult.rejectValue("nickname", "notEqual", "정보를 확인해주세요.");
            }
        }

        if(bindingResult.hasErrors()){
            return new ValidResultList(bindingResult).getList();
        }
        return null;
    }

}
