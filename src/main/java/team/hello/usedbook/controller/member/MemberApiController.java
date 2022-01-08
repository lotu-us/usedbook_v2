package team.hello.usedbook.controller.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import team.hello.usedbook.controller.service.MemberService;
import team.hello.usedbook.repository.MemberRepository;

@RestController
public class MemberApiController {
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

}
