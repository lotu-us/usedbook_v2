package team.hello.usedbook.controller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import team.hello.usedbook.domain.Member;
import team.hello.usedbook.dto.MemberDTO;
import team.hello.usedbook.repository.MemberRepository;
import team.hello.usedbook.utils.ValidResultList;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemberService {

    @Autowired MemberRepository memberRepository;

    public List loginCheck(MemberDTO.LoginForm loginForm, BindingResult bindingResult){
        ValidResultList vrl = new ValidResultList(loginForm, bindingResult);

        Member byEmail = memberRepository.findByEmail(loginForm.getEmail());
        if(byEmail == null){
            vrl.addCustomValid("email", false, "일치하는 이메일이 없습니다");
        }else{
            boolean findPassword = byEmail.getPassword().equals(loginForm.getPassword());
            if(!findPassword){
                vrl.addCustomValid("password", false, "비밀번호를 확인해주세요");
            }
        }

        //Valid 하나만 출력
        List result = new ArrayList();
        if(vrl.hasValidByField("email")){
            result = vrl.getValidByField("email");
        }else{
            if(vrl.hasValidByField("password")){
                result = vrl.getValidByField("password");
            }
        }

        return result;
    }

    public List registerCheck(MemberDTO.registerForm registerForm, BindingResult bindingResult) {
        ValidResultList vrl = new ValidResultList(registerForm, bindingResult);

        Member byEmail = memberRepository.findByEmail(registerForm.getEmail());
        if(byEmail != null){
            vrl.addCustomValid("email", false, "중복되는 이메일이 있습니다");
        }

        Member byNickName = memberRepository.findByNickName(registerForm.getNickname());
        if(byNickName != null){
            vrl.addCustomValid("nickname", false, "중복되는 닉네임입니다");
        }

        return vrl.getErrList();
    }


    /*
    원본 메세지를 알면 암호화된 메세지를 구하기는 쉽지만 암호화된 원본 메세지를 구할 수 없어야 하며 이를 '단방향성'이라고 한다.
    단방향 암호화는 복호화할 수 없는 암호화 방법이다. 복호화란 문자열을 다시 원래 문자열로 돌려놓는 것을 의미한다.

    단방향 암호화를 사용하는 이유에 대해 생각해보면, 홈페이지 비밀번호같은 경우는 복호화할 이유가 없고, 운영자들이 사용자들의 비밀번호를 알아야할 이유가 없다.
    그래서 대부분의 사이트에서 비밀번호를 잃어버렸을 때 찾으려면 비밀번호를 알려주는 것이 아니라 다시 설정해야 할 것이다.
    따라서 DB에 암호화된 비밀번호를 저장해놓고, 나중에 사용자가 로그인할 때 다시 입력받은 비밀번호를 같은 알고리즘으로 암호화해서 DB에 저장된 문자열과 비교하여 인증절차를 거치게 된다.

    문자열 hunter
    -> 해시 알고리즘 sha-256 적용 : f52fbd32b2b3b86ff88ef6c490628285f482af15ddcb29541f94bcf526a3f6c7
    -> 보완점 : 원본 메시지에 같은 알고리즘을 사용하면 매번 같은 결과가 나온다 (=같은 다이제스트가 나온다)

    -> salt(단방향 해시 함수에서 다이제스트를 생성할 때 추가되는 바이트 단위의 랜덤 문자열)
    -> salting(원본메시지에 문자열을 추가하여 다이제스트를 생성하는 행위)
    -> 기존 문자열 hunter + 랜덤 문자열 salt에 sha-256 적용
   */
    public void registerSave(MemberDTO.registerForm registerForm) {
        //비밀번호 암호화처리

        //DTO -> Entity
        Member member = new Member(
            registerForm.getEmail(),
            registerForm.getNickname(),
            registerForm.getPassword()
        );

        memberRepository.save(member);
    }
}
