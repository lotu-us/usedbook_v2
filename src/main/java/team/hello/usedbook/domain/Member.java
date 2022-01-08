package team.hello.usedbook.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter @ToString
public class Member {
    private Long id;
    private String email;
    private String nickname;
    private String password;

    private Member(){

    }

    public Member(String email, String nickname, String password){
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }
}
