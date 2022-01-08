package team.hello.usedbook.utils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    private static int min;
    private static int max;
    private static final String regexPassword = "^.*(?=^.{"+min+", "+max+"}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$";
    //숫자, 문자, 특수문자 포함 8~15자리 이내

    @Override
    public void initialize(Password constraintAnnotation) {
        //어노테이션 입력 시 파라미터로 들어온 값 초기화
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if(password.isBlank()){
            addConstraintViolation(context, "비밀번호를 입력해주세요");
            return false;
        }else{
            if(password.length() < min || password.length() > max){
                addConstraintViolation(context, "비밀번호는 "+ min +"자 ~ "+ max +"자 사이로 입력해주세요");
                return false;
            }
            return true;
        }
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String msg){
        //기본메시지 비활성화
        context.disableDefaultConstraintViolation();
        //새로운 메시지 추가
        context.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
    }
}
