package team.hello.usedbook.controller.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import team.hello.usedbook.config.SessionConstants;
import team.hello.usedbook.domain.Member;
import team.hello.usedbook.domain.dto.MemberDTO;
import team.hello.usedbook.repository.MemberRepository;

import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
class loginCheckTest {

    @Autowired private MockMvc mock;
    @Autowired private WebApplicationContext ctx;
    @Autowired private ObjectMapper objectMapper;
    @MockBean  private MemberRepository memberRepository;

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

    private MemberDTO.LoginForm createForm(String email, String password) throws Exception{
        MemberDTO.LoginForm loginForm = new MemberDTO.LoginForm(email, password);
        //return objectMapper.writeValueAsString(loginForm);
        return loginForm;
    }

    private ResultActions mockPerform(MemberDTO.LoginForm form) throws Exception {
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();

        data.add("email", form.getEmail());
        data.add("password", form.getPassword());

        ResultActions perform = mock.perform(
                MockMvcRequestBuilders.post(uri)
                .params(data)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE).accept(MediaType.APPLICATION_JSON)
        );
        return perform;
    }
    

    @Test
    @DisplayName("로그인 시 존재하지 않는 아이디 입력, 존재하지 않는 비밀번호 입력")
    void loginCheck_NotExistEmail_NotExistPassword() throws Exception {
        //given
        when(memberRepository.findByEmail("11@11")).thenReturn(
               null
        );
        MemberDTO.LoginForm form = createForm("11@11", "12");

        //when
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
        when(memberRepository.findByEmail("12@12")).thenReturn(
                new Member("12@12", "12", "12")
        );
        MemberDTO.LoginForm form = createForm("12@12", "11");

        //when
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
        when(memberRepository.findByEmail("12@12")).thenReturn(
                new Member("12@12", "12", "12")
        );
        MemberDTO.LoginForm form = createForm("12@12", "12");

        //when
        //then
        mockPerform(form)
        .andExpect(jsonPath("$.[?(@.field == 'email')]").doesNotExist())
        .andExpect(jsonPath("$.[?(@.field == 'password')]").doesNotExist())
        .andDo(print());
    }

    @Test
    @DisplayName("로그인 성공 시 세션 저장")
    void loginSuccess() throws Exception {
        //given (가짜DB)
        Member testMember  = new Member("12@12", "12", "12");
        when(memberRepository.findByEmail("12@12")).thenReturn(testMember);

        //when
        MvcResult mvcResult = mock.perform(
                MockMvcRequestBuilders.post("/login")
                        .param("email", "12@12")
                        .param("password", "12")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        )
        .andDo(print()).andReturn();

        //then
        HttpSession getSession = mvcResult.getRequest().getSession();
        Object attribute = getSession.getAttribute(SessionConstants.LOGIN_MEMBER);
        Assertions.assertThat((Member)attribute).isEqualTo(testMember);
    }

    @Test
    @DisplayName("로그인 실패 시 예외 발생")
    void loginFail() throws Exception {
        //given (가짜DB)
        Member testMember  = new Member("12@12", "12", "12");
        when(memberRepository.findByEmail("12@12")).thenReturn(testMember);

        //when
        MvcResult mvcResult = mock.perform(
                MockMvcRequestBuilders.post("/login")
                        .param("email", "12@12")
                        .param("password", "123")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        )
        .andReturn();

        Object attribute = mvcResult.getRequest().getSession().getAttribute(SessionConstants.LOGIN_MEMBER);
        Assertions.assertThat(attribute).isNull();
    }


    @Test
    @DisplayName("로그인 리다이렉트 확인")
    void login_Redirect() throws Exception {
        //given (가짜DB)
        Member testMember  = new Member("12@12", "12", "12");
        when(memberRepository.findByEmail("12@12")).thenReturn(testMember);

        //when
        mock.perform(
                MockMvcRequestBuilders.post("/login?redirectURL=/dashboard/myInfo")
                        .param("email", "12@12")
                        .param("password", "12")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        )
        .andDo(print());
    }

}