package team.hello.usedbook.controller.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team.hello.usedbook.domain.Member;
import team.hello.usedbook.domain.dto.MemberDTO;
import team.hello.usedbook.repository.MemberRepository;
import team.hello.usedbook.service.MemberService;
import team.hello.usedbook.utils.ValidResultList;

import java.util.List;

@Controller
public class MemberApiController {
    @Autowired MemberRepository memberRepository;
    @Autowired MemberService memberService;

    @PostMapping("/api/loginCheck")
    public ResponseEntity loginCheck(@Validated @RequestBody MemberDTO.LoginForm loginForm, BindingResult bindingResult){

        List<ValidResultList.ValidResult> validResults = memberService.loginCheck(loginForm, bindingResult);

        if(bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validResults);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping("/api/registerCheck")
    public ResponseEntity registerCheck(@Validated @RequestBody MemberDTO.RegisterForm registerForm, BindingResult bindingResult){

        List<ValidResultList.ValidResult> validResults = memberService.registerCheck(registerForm, bindingResult);

        if(bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validResults);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping("/api/findPasswordCheck")
    public ResponseEntity findPasswordCheck(@Validated @RequestBody MemberDTO.FindForm findForm, BindingResult bindingResult){

        List<ValidResultList.ValidResult> validResults = memberService.findPasswordCheck(findForm, bindingResult);

        if(bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validResults);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
    //==================================================================================================================================


    @PatchMapping("/api/member/{id}")
    public ResponseEntity memberUpdate(@PathVariable Long id, @Validated @RequestBody MemberDTO.UpdateForm updateForm, BindingResult bindingResult){

        Member byId = memberRepository.findById(id);

        //닉네임을 수정할때
        if(updateForm.getUpdateFieldName().equals("nickname")){

            List<ValidResultList.ValidResult> validResults = memberService.updateNicknameCheck(byId, updateForm, bindingResult);
            if(bindingResult.hasFieldErrors("nickname")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validResults);
            }

            int updateResult = memberRepository.update(id, "nickname", updateForm.getNickname());
            return ResponseEntity.status(HttpStatus.OK).body(updateResult);
        }

        //비밀번호를 수정할때
        if(updateForm.getUpdateFieldName().equals("password")){

            List<ValidResultList.ValidResult> validResults = memberService.updatePasswordCheck(byId, updateForm, bindingResult);
            if(bindingResult.hasFieldErrors("oldPassword") || bindingResult.hasFieldErrors("newPassword")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validResults);
            }

            int updateResult = memberRepository.update(id, "password", updateForm.getNewPassword());
            return ResponseEntity.status(HttpStatus.OK).body(updateResult);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 접근입니다");
    }
}
