package team.hello.usedbook.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

public class ValidResultList {

    private BindingResult bindingResult;

    @Getter @Setter         //json으로 반환하려면 getter, setter있어야함
    @AllArgsConstructor
    public static class ValidResult{
        private String field;
        private String message;
    }

    public ValidResultList(BindingResult bindingResult){
        this.bindingResult = bindingResult;
    }

    public List<ValidResult> getList(){
        List<ValidResult> result = new ArrayList<>();

        List<FieldError> fieldErrors = this.bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            ValidResult validResult = new ValidResult(
                    fieldError.getField(), fieldError.getDefaultMessage()
            );
            result.add(validResult);
        }

        return result;
    }
}
