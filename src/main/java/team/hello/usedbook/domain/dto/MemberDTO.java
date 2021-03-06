package team.hello.usedbook.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import team.hello.usedbook.utils.Password;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class MemberDTO {

    @Data
    @AllArgsConstructor
    public static class LoginForm{
        @NotBlank
        @Email
        @Size(max = 40)
        private String email;

        @NotBlank(message = "비밀번호를 입력해주세요")
        @Password(min=2, max=20)
        private String password;
    }

    @Data
    @AllArgsConstructor
    public static class RegisterForm {
        @NotBlank
        @Email
        @Size(max = 40)
        private String email;

        @NotBlank
        @Size(min=2, max=20, message = "닉네임은 2자 ~ 20자 사이로 입력해주세요")
        @Pattern(regexp = "^[0-9a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣_]*$", message = "특수문자는 불가능합니다")    //영어,숫자,한글, _만 가능
        private String nickname;

        @NotBlank(message = "비밀번호를 입력해주세요")
        @Password(min=2, max=20)
        private String password;
    }

    @Data
    @AllArgsConstructor
    public static class FindForm {
        private String email;
        private String nickname;
    }

    @Data
    public static class UpdateForm {

        @NotBlank
        private String updateFieldName;

        @Size(min=2, max=20, message = "닉네임은 2자 ~ 20자 사이로 입력해주세요")
        @Pattern(regexp = "^[0-9a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣_]*$", message = "특수문자는 불가능합니다")    //영어,숫자,한글, _만 가능
        private String nickname = "";

        @Password(min=2, max=20)
        private String oldPassword = "";

        @Password(min=2, max=20)
        private String newPassword = "";
    }

}
