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
import team.hello.usedbook.dto.MemberDTO;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
class loginCheckTest {

    @Autowired private MockMvc mock;
    @Autowired private WebApplicationContext ctx;
    @Autowired private ObjectMapper objectMapper;

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

    String uri = "/loginCheck";

    private String createForm(String email, String password) throws Exception{
        MemberDTO.LoginForm loginForm = new MemberDTO.LoginForm(email, password);
        return objectMapper.writeValueAsString(loginForm);
    }

    private ResultActions mockPerform(String formdata) throws Exception {
        ResultActions perform = mock.perform(
                MockMvcRequestBuilders.post(uri).content(formdata)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        );
        return perform;
    }
    

    @Test
    @DisplayName("로그인 시 존재하지 않는 아이디 입력, 존재하지 않는 비밀번호 입력")
    void loginCheck_NotExistEmail_NotExistPassword() throws Exception {
        //given
        String form = createForm("11@111", "12");

        //then
        mockPerform(form)
        .andExpect(jsonPath("$.[?(@.field == 'email')]").exists())
        .andExpect(jsonPath("$.[?(@.field == 'password')]").doesNotExist())
        .andDo(print());
    }

    @Test
    @DisplayName("로그인 시 존재하는 아이디 입력, 존재하지 않는 비밀번호 입력")
    void loginCheck_ExistEmail_NotExistPassword() throws Exception {
        //given
        String form = createForm("11@11", "12");

        //then
        mockPerform(form)
        .andExpect(jsonPath("$.[?(@.field == 'email')]").doesNotExist())
        .andExpect(jsonPath("$.[?(@.field == 'password')]").exists())
        .andDo(print());
    }

    @Test
    @DisplayName("로그인 시 존재하는 아이디 입력, 존재하는 비밀번호 입력")
    void loginCheck_ExistEmail_ExistPassword() throws Exception {
        //given
        String form = createForm("11@11", "11");

        //then
        mockPerform(form)
        .andExpect(jsonPath("$.[?(@.field == 'email')]").doesNotExist())
        .andExpect(jsonPath("$.[?(@.field == 'password')]").doesNotExist())
        .andDo(print());
    }


}