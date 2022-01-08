package team.hello.usedbook.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Getter
public class ValidResultList {

    @Getter
    @AllArgsConstructor
    public static class ValidResult{
        private String field;
        private boolean status;
        private String message;
    }

    private List<ValidResult> errList = new ArrayList<>();

    private ValidResultList(){    }

    //공통 valid 추가
    public ValidResultList(Object object, BindingResult bindingResult){
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();

            FieldError fieldError = bindingResult.getFieldError(fieldName);
            if(fieldError != null){
                String message = fieldError.getDefaultMessage();
                this.errList.add(new ValidResult(fieldName, false, message));
            }
        }
    }


    //커스텀 valid 추가
    public void addCustomValid(String fieldName, boolean status, String message){
        ValidResult validResult = this.errList.stream()
                .filter(array -> array.getField().equals(fieldName))
                .findFirst()
                .orElse(null);

        if(validResult == null){    //공통 valid내용이 없는 경우에만 추가
            this.errList.add(new ValidResult(fieldName, status, message));
        }
    }


    public List getValidByField(String fieldName){
        List<ValidResult> collect = this.errList.stream()
                .filter(array -> array.getField().equals(fieldName))
                .collect(Collectors.toList());

        return collect;
    }

    public boolean hasValidByField(String fieldName){
        ValidResult validResult = this.errList.stream()
                .filter(array -> array.getField().equals(fieldName))
                .findFirst()
                .orElse(null);

        if(validResult != null){
            return true;
        }else{
            return false;
        }
    }

}
