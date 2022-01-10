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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import team.hello.usedbook.domain.Member;
import team.hello.usedbook.domain.dto.MemberDTO;
import team.hello.usedbook.repository.MemberRepository;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


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

    String uri = "/findPasswordCheck";

    private ResultActions mockPerform(MemberDTO.FindForm form) throws Exception {
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();

        data.add("email", form.getEmail());
        data.add("nickname", form.getNickname());

        ResultActions perform = mock.perform(
                MockMvcRequestBuilders.post(uri)
                .params(data)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE).accept(MediaType.APPLICATION_JSON)
        );
        return perform;
    }
    

    @Test
    @DisplayName("비밀번호 찾기 - 없는 이메일 입력")
    void findPassword_NotExistEmail() throws Exception {
        //given
        when(memberRepository.findByEmail("11@11")).thenReturn(
               null
        );
        MemberDTO.FindForm form = new MemberDTO.FindForm("11@121", "12");

        //when
        //then
        mockPerform(form)
        .andExpect(jsonPath("$.[?(@.field == 'email')]").exists())
        .andExpect(jsonPath("$.[?(@.field == 'nickname')]").doesNotExist())
        .andDo(print());
    }


    @Test
    @DisplayName("비밀번호 찾기 - 존재하는 이메일 입력, 틀린 닉네임 입력")
    void findPassword_ExistEmail_WrongNickname() throws Exception {
        //given
        when(memberRepository.findByEmail("11@11")).thenReturn(
                new Member("11@11", "11", "11")
        );
        MemberDTO.FindForm form = new MemberDTO.FindForm("11@11", "12");

        //when
        //then
        mockPerform(form)
                .andExpect(jsonPath("$.[?(@.field == 'email')]").doesNotExist())
                .andExpect(jsonPath("$.[?(@.field == 'nickname')]").exists())
                .andDo(print());
    }

    @Test
    @DisplayName("비밀번호 찾기 - 존재하는 이메일, 닉네임 입력")
    void findPassword_ExistEmail_ExistNickname() throws Exception {
        //given
        when(memberRepository.findByEmail("11@11")).thenReturn(
                new Member("11@11", "11", "11")
        );
        MemberDTO.FindForm form = new MemberDTO.FindForm("11@11", "11");

        //when
        //then
        mockPerform(form)
                .andExpect(jsonPath("$.[?(@.field == 'email')]").doesNotExist())
                .andExpect(jsonPath("$.[?(@.field == 'nickname')]").doesNotExist())
                .andDo(print());
    }

}