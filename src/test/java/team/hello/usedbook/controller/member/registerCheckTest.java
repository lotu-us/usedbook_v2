package team.hello.usedbook.controller.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import team.hello.usedbook.domain.dto.MemberDTO;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class registerCheckTest {

    //request, response 테스트
    @Autowired private MockMvc mock;
    @Autowired private WebApplicationContext ctx;
    @Autowired private ObjectMapper objectMapper;

    //필드 validation 테스트
    private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private Validator validator = factory.getValidator();


    @BeforeEach
    public void setUp() {
        objectMapper = Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .modules(new JavaTimeModule())
                .build();

        mock = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }
    //참고
    //https://github.com/json-path/JsonPath
    //https://www.codetd.com/ko/article/6300874  ****
    //http://jsonpath.herokuapp.com/?path=$.store.book[*].author


    String uri = "/registerCheck";

    private MemberDTO.RegisterForm createForm(String email, String nickname, String password) throws Exception{
        MemberDTO.RegisterForm registerForm = new MemberDTO.RegisterForm(email, nickname, password);
        //return objectMapper.writeValueAsString(registerForm);
        return registerForm;
    }

    private ResultActions mockPerform(MemberDTO.RegisterForm form) throws Exception {
        ResultActions perform = mock.perform(
                MockMvcRequestBuilders.post(uri)
                .param("email", form.getEmail())
                .param("nickname", form.getNickname())
                .param("password", form.getPassword())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .accept(MediaType.APPLICATION_JSON)
        );
        return perform;
    }


    @Test
    @DisplayName("회원가입폼 필드 valid 확인")
    void registerForm_FieldValidCheck() throws Exception {
        //given
        MemberDTO.RegisterForm registerForm[] = {
            new MemberDTO.RegisterForm("", "", ""),                 //모두 빈문자
            new MemberDTO.RegisterForm(" ", " ", " "),              //모두 공백 1자리
            new MemberDTO.RegisterForm("re@1333333333333333333333333333333333333333333333333333333333", "133333333333333333333333333", "testregister!1233333333333333333333333"), //모두 최대길이 넘을때
            new MemberDTO.RegisterForm("1", "1", "1"),              //모두 1자리
            new MemberDTO.RegisterForm("re@", "1e", "1s@"),          //이메일 양식 확인 (에러발생)
            new MemberDTO.RegisterForm("re@1", "1e", "1s@"),         //이메일 양식 확인 (에러없음)
            new MemberDTO.RegisterForm("re@1", "1eㅎㅎ", "1s@"),     //닉네임 영어 숫자 한글 가능 (에러없음)
            new MemberDTO.RegisterForm("re@1", "1e#", "1s@"),        //닉네임 특수문자 확인 (에러발생)
            new MemberDTO.RegisterForm("re@1", "1e", "1sㅎ"),         //비밀번호 숫자, 영어, 특수문자 각각 1개 이상 포함 (한글 에러발생?)
            new MemberDTO.RegisterForm("re@1", "1e", "121241"),         //비밀번호 숫자로만
            new MemberDTO.RegisterForm("re@1", "1e", "eflkwjefiwoe"),     //비밀번호 영어로만
            new MemberDTO.RegisterForm("re@1", "1e", "#$@*(@#*"),     //비밀번호 특수문자로만
            new MemberDTO.RegisterForm("re@1", "13", "testregister!123"), //비밀번호 숫자, 영어, 특수문자 각각 1개 이상 포함 (에러없음)
        };

        //then
        int count=1;
        for (MemberDTO.RegisterForm form : registerForm) {
            Set<ConstraintViolation<MemberDTO.RegisterForm>> violations = validator.validate(form);

            // 검증 결과를 출력
            System.out.println("=== ["+count+"] ====================================================");
            for (ConstraintViolation<MemberDTO.RegisterForm> violation : violations) {
                String field = violation.getPropertyPath().toString();
                String message = violation.getMessage();
                System.out.println(field+" : "+message);
            }
            count++;
        }
    }

    @Test
    @DisplayName("회원가입 시 아이디 중복, 닉네임 중복")
    void registerCheck_DuplicateId_DuplicateNickname() throws Exception {
        //given
        MemberDTO.RegisterForm form = createForm("11@11", "11", "11");

        //then
        mockPerform(form)
        .andExpect(jsonPath("$.[?(@.field == 'email')]").exists())
        .andExpect(jsonPath("$.[?(@.field == 'nickname')]").exists())
        .andExpect(jsonPath("$.[?(@.field == 'password')]").doesNotExist())
        .andDo(print());
        //[{"field":"email","message":"이미 존재하는 이메일입니다."},{"field":"nickname","message":"이미 존재하는 닉네임입니다."}]
    }

    @Test
    @DisplayName("회원가입 시 아이디 중복아님, 닉네임 중복아님")
    void registerCheck_NotDuplicateId_NotDuplicateNickname() throws Exception {
        //given
        MemberDTO.RegisterForm form = createForm("11@13", "13", "11");

        //then
        mockPerform(form)
        .andExpect(jsonPath("$.[?(@.field == 'email')]").doesNotExist())
        .andExpect(jsonPath("$.[?(@.field == 'nickname')]").doesNotExist())
        .andExpect(jsonPath("$.[?(@.field == 'password')]").doesNotExist())
        .andDo(print());
    }

}
