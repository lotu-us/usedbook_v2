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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import team.hello.usedbook.domain.Member;
import team.hello.usedbook.domain.dto.MemberDTO;
import team.hello.usedbook.repository.MemberRepository;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class findPasswordTest {

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

    String uri = "/api/findPasswordCheck";

    private String createForm(String email, String nickname) throws Exception{
        MemberDTO.FindForm findForm = new MemberDTO.FindForm(email, nickname);
        return objectMapper.writeValueAsString(findForm);
    }

    private ResultActions mockPerform(String form) throws Exception {
        ResultActions perform = mock.perform(
                MockMvcRequestBuilders.post(uri)
                .content(form)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        );
        return perform;
    }
    

    @Test
    @DisplayName("실패 - 없는 이메일 입력")
    void findPassword_NotExistEmail() throws Exception {
        //given
        when(memberRepository.findByEmail("11@11")).thenReturn(
               null
        );
        String form = createForm("11@121", "12");

        //when
        //then
        mockPerform(form)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.[?(@.field == 'email')]").exists())
        .andExpect(jsonPath("$.[?(@.field == 'nickname')]").doesNotExist())
        .andDo(print());
    }


    @Test
    @DisplayName("실패 - 존재하는 이메일 입력, 틀린 닉네임 입력")
    void findPassword_ExistEmail_WrongNickname() throws Exception {
        //given
        when(memberRepository.findByEmail("11@11")).thenReturn(
                new Member("11@11", "11", "11")
        );
        String form = createForm("11@11", "12");

        //when
        //then
        mockPerform(form)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.[?(@.field == 'email')]").doesNotExist())
        .andExpect(jsonPath("$.[?(@.field == 'nickname')]").exists())
        .andDo(print());
    }

    @Test
    @DisplayName("성공 - 존재하는 이메일, 닉네임 입력")
    void findPassword_ExistEmail_ExistNickname() throws Exception {
        //given
        when(memberRepository.findByEmail("11@11")).thenReturn(
                new Member("11@11", "11", "11")
        );
        String form = createForm("11@11", "11");

        //when
        //then
        mockPerform(form)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[?(@.field == 'email')]").doesNotExist())
        .andExpect(jsonPath("$.[?(@.field == 'nickname')]").doesNotExist())
        .andDo(print());
    }

    @Test
    @DisplayName("실패 - 비밀번호 찾기 시 api를 거치지 않아 잘못된 값이 들어오면 다시 비밀번호 찾기 화면으로")
    void registerFail() throws Exception {
        //given
        MvcResult mvcResult = mock.perform(
                MockMvcRequestBuilders.post("/findPassword")
                        .param("email", "12@")
                        .param("nickname", "12#3")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        )
        .andExpect(status().isOk())
        .andExpect(view().name("member/findPassword"))
        .andReturn();
    }
}