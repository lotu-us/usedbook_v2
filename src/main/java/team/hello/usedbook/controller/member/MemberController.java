package team.hello.usedbook.controller.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team.hello.usedbook.controller.service.MemberService;
import team.hello.usedbook.dto.MemberDTO;

import java.util.List;

@Slf4j
@Controller
public class MemberController {

    @Autowired MemberService memberService;

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
        return memberService.loginCheck(loginForm, bindingResult);
    }


    @GetMapping("/register")
    public String registerForm(){
        return "member/register";
    }

    @PostMapping("/register")
    public String registerSave(@Validated @ModelAttribute MemberDTO.registerForm registerForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "member/register";
        }

        memberService.registerSave(registerForm);
        return "redirect:/registerOk";
    }

    @PostMapping("/registerCheck")
    @ResponseBody
    public List registerCheck(@Validated @RequestBody MemberDTO.registerForm registerForm, BindingResult bindingResult){
        return memberService.registerCheck(registerForm, bindingResult);
    }

    @GetMapping("/registerOk")
    public String registerOk(){
        return "member/registerOk";
    }



    @GetMapping("/findPassword")
    public String findPasswordForm(){
        return "member/findPassword";
    }
}
