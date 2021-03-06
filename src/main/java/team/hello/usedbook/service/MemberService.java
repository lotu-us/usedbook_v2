package team.hello.usedbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import team.hello.usedbook.domain.Member;
import team.hello.usedbook.domain.dto.MemberDTO;
import team.hello.usedbook.repository.MemberRepository;
import team.hello.usedbook.utils.CustomMailSender;
import team.hello.usedbook.utils.ValidResultList;

import java.util.List;
import java.util.UUID;

@Service
public class MemberService {

    @Autowired MemberRepository memberRepository;
    @Autowired CustomMailSender mailSender;

    public List<ValidResultList.ValidResult> loginCheck(MemberDTO.LoginForm loginForm, BindingResult bindingResult) {
        //커스텀 오류 추가
        Member byEmail = memberRepository.findByEmail(loginForm.getEmail());
        if(byEmail == null){
            bindingResult.rejectValue("email", "notExist", "존재하지 않는 이메일입니다.");
        }else{
            boolean password = byEmail.getPassword().equals(loginForm.getPassword());
            if(!password){
                bindingResult.rejectValue("password", "notEqual", "비밀번호를 확인해주세요.");
            }
        }

        List<ValidResultList.ValidResult> list = new ValidResultList(bindingResult).getList();
        return list;
    }

    public List<ValidResultList.ValidResult> registerCheck(MemberDTO.RegisterForm registerForm, BindingResult bindingResult) {
        Member byEmail = memberRepository.findByEmail(registerForm.getEmail());
        if(byEmail != null){
            bindingResult.rejectValue("email", "duplicate", "이미 존재하는 이메일입니다.");
        }

        Member byNickName = memberRepository.findByNickName(registerForm.getNickname());
        if(byNickName != null){
            bindingResult.rejectValue("nickname", "duplicate", "이미 존재하는 닉네임입니다.");
        }

        List<ValidResultList.ValidResult> list = new ValidResultList(bindingResult).getList();
        return list;
    }

    public List<ValidResultList.ValidResult> findPasswordCheck(MemberDTO.FindForm findForm, BindingResult bindingResult) {
        Member byEmail = memberRepository.findByEmail(findForm.getEmail());
        if(byEmail == null){
            bindingResult.rejectValue("email", "notExist", "존재하지 않는 이메일입니다.");
        }else{
            boolean nickname = byEmail.getNickname().equals(findForm.getNickname());
            if(!nickname){
                bindingResult.rejectValue("nickname", "notEqual", "정보를 확인해주세요.");
            }
        }

        List<ValidResultList.ValidResult> list = new ValidResultList(bindingResult).getList();
        return list;
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
    public void registerSave(MemberDTO.RegisterForm registerForm) {
        //비밀번호 암호화처리

        //DTO -> Entity
        Member member = new Member(
                registerForm.getEmail(),
                registerForm.getNickname(),
                registerForm.getPassword()
        );

        memberRepository.save(member);
    }

    public String createTempPasswordAndSendMail(Member byEmail) {
        //임시 비밀번호
        UUID uid = UUID.randomUUID();
        String tempPassword = uid.toString().substring(0,8);

        String html =
                "<div>" +
                        byEmail.getNickname()+"님의 임시 비밀번호는 <span style='font-weight:bold; color:blue;'>"+tempPassword+"</span> 입니다." +
                        "</div>";

        CustomMailSender.MailDTO mailDTO = new CustomMailSender.MailDTO();
        mailDTO.setTitle("[책방] 임시 비밀번호 안내 메일입니다.");
        mailDTO.setMessage(html);
        mailDTO.setAddress(byEmail.getEmail());
        mailSender.mimeMailSend(mailDTO);

        return tempPassword;
    }


    public List<ValidResultList.ValidResult> updateNicknameCheck(Member byId, MemberDTO.UpdateForm updateForm, BindingResult bindingResult) {
        //현재 닉네임과 같으면 안됨
        boolean oldnewEqual = byId.getNickname().equals(updateForm.getNickname());
        if(oldnewEqual){
            bindingResult.rejectValue("nickname", "notChange", "현재 닉네임과 같습니다");
        }

        //DB에서 유일한 닉네임이면 수정 가능
        Member nicknameExists = memberRepository.findByNickName(updateForm.getNickname());
        if(nicknameExists != null){
            bindingResult.rejectValue("nickname", "duplicate", "중복되는 닉네임입니다");
        }

        List<ValidResultList.ValidResult> list = new ValidResultList(bindingResult).getList("nickname");
        return list;
    }


    public List<ValidResultList.ValidResult> updatePasswordCheck(Member byId, MemberDTO.UpdateForm updateForm, BindingResult bindingResult) {

        //현재 비밀번호가 일치해야함
        boolean oldPasswordCorrect = updateForm.getOldPassword().equals(byId.getPassword());
        if(!oldPasswordCorrect){
            bindingResult.rejectValue("oldPassword", "notEqual", "현재 비밀번호가 일치하지 않습니다");
            return new ValidResultList(bindingResult).getList("oldPassword");
        }

        //새 비밀번호는 현재 비밀번호와 같으면 안됨
        boolean oldnewEqual = byId.getPassword().equals(updateForm.getNewPassword());
        if(oldnewEqual){
            bindingResult.rejectValue("newPassword", "notChange", "현재 비밀번호와 같습니다");
            return new ValidResultList(bindingResult).getList("newPassword");
        }

        return new ValidResultList(bindingResult).getList("newPassword");
    }
}
