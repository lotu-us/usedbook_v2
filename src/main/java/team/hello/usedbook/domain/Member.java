package team.hello.usedbook.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter @ToString
@NoArgsConstructor @AllArgsConstructor
public class Member {
    private Long id;
    private String email;
    private String nickname;
    private String password;
}
