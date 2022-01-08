package team.hello.usedbook.controller.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team.hello.usedbook.controller.service.MemberService;
import team.hello.usedbook.domain.Member;
import team.hello.usedbook.dto.MemberDTO;
import team.hello.usedbook.repository.MemberRepository;
import team.hello.usedbook.utils.ValidResultList;

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
    public String loginOk(@Validated @ModelAttribute MemberDTO.LoginForm loginForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "member/login";
        }
        //session 기능 추가
        return "redirect:/";
    }

    @PostMapping("/loginCheck")
    @ResponseBody
    public List loginCheck(@Validated @RequestBody MemberDTO.LoginForm loginForm, BindingResult bindingResult){
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

    //=================================================================================================
    @GetMapping("/register")
    public String registerForm(){
        return "member/register";
    }

    @PostMapping("/register")
    public String registerSave(@Validated @ModelAttribute MemberDTO.RegisterForm registerForm){
        memberService.registerSave(registerForm);
        return "redirect:/registerOk";
    }

    @PostMapping("/registerCheck")
    @ResponseBody
    public List registerCheck(@Validated @RequestBody MemberDTO.RegisterForm registerForm, BindingResult bindingResult){
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

    //=================================================================================================
    @GetMapping("/findPassword")
    public String findPasswordForm(){
        return "member/findPassword";
    }
}
