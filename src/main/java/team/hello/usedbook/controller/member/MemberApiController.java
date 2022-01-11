package team.hello.usedbook.controller.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import team.hello.usedbook.domain.Member;
import team.hello.usedbook.domain.dto.MemberDTO;
import team.hello.usedbook.repository.MemberRepository;
import team.hello.usedbook.service.MemberService;
import team.hello.usedbook.utils.ValidResultList;

@Controller
public class MemberApiController {
    @Autowired MemberRepository memberRepository;
    @Autowired MemberService memberService;

    @PatchMapping("/api/member/{id}")
    @ResponseBody
    public Object memberUpdate(@PathVariable Long id, @Validated @RequestBody MemberDTO.UpdateForm updateForm, BindingResult bindingResult){

        Member byId = memberRepository.findById(id);

        //닉네임을 수정할때
        if(updateForm.getUpdateFieldName().equals("nickname")){

            //닉네임에 값이 있고 에러가 없을때
            if(bindingResult.hasFieldErrors("nickname")){
                return new ValidResultList(bindingResult).getList("nickname");
            }else{

                //현재 닉네임과 같으면 안됨
                boolean oldnewEqual = byId.getNickname().equals(updateForm.getNickname());
                if(oldnewEqual){
                    bindingResult.rejectValue("nickname", "notChange", "현재 닉네임과 같습니다");
                    return new ValidResultList(bindingResult).getList("nickname");
                }

                //DB에서 유일한 닉네임이면 수정 가능
                Member nicknameExists = memberRepository.findByNickName(updateForm.getNickname());
                if(nicknameExists == null){
                    int updateResult = memberRepository.update(id, "nickname", updateForm.getNickname());
                    return updateResult;
                }else{
                    bindingResult.rejectValue("nickname", "duplicate", "중복되는 닉네임입니다");
                    return new ValidResultList(bindingResult).getList("nickname");
                }
            }
        }


        //비밀번호를 수정할때
        if(updateForm.getUpdateFieldName().equals("password")){

            //현재 비밀번호가 일치해야함
            boolean oldPasswordCorrect = updateForm.getOldPassword().equals(byId.getPassword());
            if(!oldPasswordCorrect){
                bindingResult.rejectValue("oldPassword", "notEqual", "현재 비밀번호가 일치하지 않습니다");
                return new ValidResultList(bindingResult).getList("oldPassword");
            }

            //새 비밀번호에 값이 있고 에러가 없어야함
            if(bindingResult.hasFieldErrors("newPassword")){
                return new ValidResultList(bindingResult).getList("newPassword");
            }

            //현재 비밀번호와 같으면 안됨
            boolean oldnewEqual = byId.getPassword().equals(updateForm.getNewPassword());
            if(oldnewEqual){
                bindingResult.rejectValue("newPassword", "notChange", "현재 비밀번호와 같습니다");
                return new ValidResultList(bindingResult).getList("newPassword");
            }else{
                int updateResult = memberRepository.update(id, "password", updateForm.getNewPassword());
                return updateResult;
            }

        }

        return null;
    }
}
