package team.hello.usedbook.utils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    private int min;
    private int max;
    private String regex = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{"+min+",}$";   //최소 8자리에 숫자, 영어, 특수문자 각각 1개 이상 포함
    Matcher matcher;

    @Override
    public void initialize(Password constraintAnnotation) {
        //어노테이션 입력 시 파라미터로 들어온 값 초기화
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        if(password.length() < min || password.length() > max){
            addConstraintViolation(context, "비밀번호는 "+ min +"자 ~ "+ max +"자 사이로 입력해주세요");
            return false;
        }

        //숫자, 문자, 특수문자 각각 1개 이상 포함
        matcher = Pattern.compile(regex).matcher(password);
        if(!matcher.matches()) {
            addConstraintViolation(context, "비밀번호는 숫자, 영어, 특수문자 각각 1개 이상 포함하고 "+min+"자 ~ "+ max +"자로 입력해주세요");
            return false;
        }

        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String msg){
        //기본메시지 비활성화
        context.disableDefaultConstraintViolation();
        //새로운 메시지 추가
        context.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
    }
}
