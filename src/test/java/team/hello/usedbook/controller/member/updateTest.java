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
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import team.hello.usedbook.config.SessionConstants;
import team.hello.usedbook.domain.Member;
import team.hello.usedbook.domain.dto.MemberDTO;
import team.hello.usedbook.repository.MemberRepository;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class updateTest {
    @Autowired private MockMvc mock;
    @Autowired private WebApplicationContext ctx;
    @Autowired private ObjectMapper objectMapper;
    @MockBean  private MemberRepository memberRepository;
    @MockBean  private MockHttpSession session;

    private Member member;

    @BeforeEach
    public void setUp() {
        objectMapper = Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .modules(new JavaTimeModule())
                .build();

        mock = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();

        member = new Member("12@12", "12", "12");
        member.addIdForTest(2L);

        session = new MockHttpSession();
        session.setAttribute(SessionConstants.LOGIN_MEMBER, member);
    }


    @Test
    @DisplayName("수정 - 닉네임 글자수 미달")
    void nickName_valid_minsize() throws Exception {
        //given
        when(memberRepository.findById(2L)).thenReturn(member);

        //when
        String uri = "/api/member/2";
        MemberDTO.UpdateForm updateForm = new MemberDTO.UpdateForm();
        updateForm.setUpdateFieldName("nickname");
        updateForm.setNickname("1");
        String content = objectMapper.writeValueAsString(updateForm);
        //String content = "{\"field\":\"nickname\", \"value\":\"1313\"}";

        //then
        mock.perform(
                MockMvcRequestBuilders.patch(uri).session(session).content(content)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.[?(@.field == 'nickname')]").exists())
        .andExpect(jsonPath("$..message").value("닉네임은 2자 ~ 20자 사이로 입력해주세요"))
        .andExpect(jsonPath("$.[?(@.field == 'oldPassword')]").doesNotExist())
        .andExpect(jsonPath("$.[?(@.field == 'newPassword')]").doesNotExist())
        .andDo(print());
    }


    @Test
    @DisplayName("수정 - 닉네임 공백 입력")
    void nickName_valid_special_character() throws Exception {
        //given
        when(memberRepository.findById(2L)).thenReturn(member);

        //when
        String uri = "/api/member/2";
        MemberDTO.UpdateForm updateForm = new MemberDTO.UpdateForm();
        updateForm.setUpdateFieldName("nickname");
        updateForm.setNickname(" 12 ");
        String content = objectMapper.writeValueAsString(updateForm);

        //then
        mock.perform(
                MockMvcRequestBuilders.patch(uri).session(session).content(content)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.[?(@.field == 'nickname')]").exists())
        .andExpect(jsonPath("$..message").value("특수문자는 불가능합니다"))
        .andExpect(jsonPath("$.[?(@.field == 'oldPassword')]").doesNotExist())
        .andExpect(jsonPath("$.[?(@.field == 'newPassword')]").doesNotExist())
        .andDo(print());
    }


    @Test
    @DisplayName("수정 - 닉네임 중복")
    void nickName_duplicate() throws Exception {
        //given
        when(memberRepository.findById(2L)).thenReturn(member);
        when(memberRepository.findByNickName("13")).thenReturn(new Member("11@13", "13", "13"));

        //when
        String uri = "/api/member/2";
        MemberDTO.UpdateForm updateForm = new MemberDTO.UpdateForm();
        updateForm.setUpdateFieldName("nickname");
        updateForm.setNickname("13");
        String content = objectMapper.writeValueAsString(updateForm);
        //String content = "{\"field\":\"nickname\", \"value\":\"1313\"}";

        //then
        mock.perform(
                MockMvcRequestBuilders.patch(uri).session(session).content(content)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.[?(@.field == 'nickname')]").exists())
        .andExpect(jsonPath("$..message").value("중복되는 닉네임입니다"))
        .andExpect(jsonPath("$.[?(@.field == 'oldPassword')]").doesNotExist())
        .andExpect(jsonPath("$.[?(@.field == 'newPassword')]").doesNotExist())
        .andDo(print());
    }

    @Test
    @DisplayName("수정 - 닉네임 현재와 같음")
    void nickName_not_change() throws Exception {
        //given
        when(memberRepository.findById(2L)).thenReturn(member);

        //when
        String uri = "/api/member/2";
        MemberDTO.UpdateForm updateForm = new MemberDTO.UpdateForm();
        updateForm.setUpdateFieldName("nickname");
        updateForm.setNickname("12");
        String content = objectMapper.writeValueAsString(updateForm);
        //String content = "{\"field\":\"nickname\", \"value\":\"1313\"}";

        //then
        mock.perform(
                MockMvcRequestBuilders.patch(uri).session(session).content(content)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.[?(@.field == 'nickname')]").exists())
        .andExpect(jsonPath("$..message").value("현재 닉네임과 같습니다"))
        .andExpect(jsonPath("$.[?(@.field == 'oldPassword')]").doesNotExist())
        .andExpect(jsonPath("$.[?(@.field == 'newPassword')]").doesNotExist())
        .andDo(print());
    }


    @Test
    @DisplayName("수정 - 닉네임 수정 성공")
    void nickName_update_success() throws Exception {
        //given
        when(memberRepository.findById(2L)).thenReturn(member);

        //when
        String uri = "/api/member/2";
        MemberDTO.UpdateForm updateForm = new MemberDTO.UpdateForm();
        updateForm.setUpdateFieldName("nickname");
        updateForm.setNickname("13");
        String content = objectMapper.writeValueAsString(updateForm);
        //String content = "{\"field\":\"nickname\", \"value\":\"1313\"}";

        //then
        mock.perform(
                MockMvcRequestBuilders.patch(uri).session(session).content(content)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[?(@.field == 'nickname')]").doesNotExist())
        .andExpect(jsonPath("$.[?(@.field == 'oldPassword')]").doesNotExist())
        .andExpect(jsonPath("$.[?(@.field == 'newPassword')]").doesNotExist())
        .andDo(print());
    }

    @Test
    @DisplayName("수정 - 현재 비밀번호 일치 안함")
    void oldPassword_not_equal() throws Exception {
        //given
        when(memberRepository.findById(2L)).thenReturn(member);

        //when
        String uri = "/api/member/2";
        MemberDTO.UpdateForm updateForm = new MemberDTO.UpdateForm();
        updateForm.setUpdateFieldName("password");
        updateForm.setOldPassword("11");
        String content = objectMapper.writeValueAsString(updateForm);
        //String content = "{\"field\":\"nickname\", \"value\":\"1313\"}";

        //then
        mock.perform(
                MockMvcRequestBuilders.patch(uri).session(session).content(content)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.[?(@.field == 'nickname')]").doesNotExist())
        .andExpect(jsonPath("$.[?(@.field == 'oldPassword')]").exists())
            .andExpect(jsonPath("$..message").value("현재 비밀번호가 일치하지 않습니다"))
        .andExpect(jsonPath("$.[?(@.field == 'newPassword')]").doesNotExist())
        .andDo(print());
    }

    @Test
    @DisplayName("수정 - 현재 비밀번호 일치, 새 비밀번호 자리수 미달")
    void oldPassword_equal_newPassword_min_size() throws Exception {
        //given
        when(memberRepository.findById(2L)).thenReturn(member);

        //when
        String uri = "/api/member/2";
        MemberDTO.UpdateForm updateForm = new MemberDTO.UpdateForm();
        updateForm.setUpdateFieldName("password");
        updateForm.setOldPassword("12");
        updateForm.setNewPassword("1");
        String content = objectMapper.writeValueAsString(updateForm);
        //String content = "{\"field\":\"nickname\", \"value\":\"1313\"}";

        //then
        mock.perform(
                MockMvcRequestBuilders.patch(uri).session(session).content(content)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.[?(@.field == 'nickname')]").doesNotExist())
        .andExpect(jsonPath("$.[?(@.field == 'oldPassword')]").doesNotExist())
        .andExpect(jsonPath("$.[?(@.field == 'newPassword')]").exists())
                .andExpect(jsonPath("$..message").value("비밀번호는 2자 ~ 20자 사이로 입력해주세요"))
        .andDo(print());
    }

    @Test
    @DisplayName("수정 - 현재 비밀번호 일치, 새 비밀번호 현재 비밀번호와 동일")
    void oldPassword_equal_newPassword() throws Exception {
        //given
        when(memberRepository.findById(2L)).thenReturn(member);

        //when
        String uri = "/api/member/2";
        MemberDTO.UpdateForm updateForm = new MemberDTO.UpdateForm();
        updateForm.setUpdateFieldName("password");
        updateForm.setOldPassword("12");
        updateForm.setNewPassword("12");
        String content = objectMapper.writeValueAsString(updateForm);
        //String content = "{\"field\":\"nickname\", \"value\":\"1313\"}";

        //then
        mock.perform(
                MockMvcRequestBuilders.patch(uri).session(session).content(content)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.[?(@.field == 'nickname')]").doesNotExist())
        .andExpect(jsonPath("$.[?(@.field == 'oldPassword')]").doesNotExist())
        .andExpect(jsonPath("$.[?(@.field == 'newPassword')]").exists())
                .andExpect(jsonPath("$..message").value("현재 비밀번호와 같습니다"))
        .andDo(print());
    }

    @Test
    @DisplayName("수정 - 비밀번호 수정 성공")
    void oldPassword_not_equal_newPassword() throws Exception {
        //given
        when(memberRepository.findById(2L)).thenReturn(member);

        //when
        String uri = "/api/member/2";
        MemberDTO.UpdateForm updateForm = new MemberDTO.UpdateForm();
        updateForm.setUpdateFieldName("password");
        updateForm.setOldPassword("12");
        updateForm.setNewPassword("122");
        String content = objectMapper.writeValueAsString(updateForm);
        //String content = "{\"field\":\"nickname\", \"value\":\"1313\"}";

        //then
        mock.perform(
                MockMvcRequestBuilders.patch(uri).session(session).content(content)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[?(@.field == 'nickname')]").doesNotExist())
        .andExpect(jsonPath("$.[?(@.field == 'oldPassword')]").doesNotExist())
        .andExpect(jsonPath("$.[?(@.field == 'newPassword')]").doesNotExist())
        .andDo(print());
    }

}