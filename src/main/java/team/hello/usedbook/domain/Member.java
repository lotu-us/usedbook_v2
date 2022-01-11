package team.hello.usedbook.domain;

import lombok.Getter;
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


    //test용
    public void addIdForTest(Long id){
        this.id = id;
    }
}
