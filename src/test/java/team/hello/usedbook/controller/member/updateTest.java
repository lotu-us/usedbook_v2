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
    @DisplayName("?????? - ????????? ????????? ??????")
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
        .andExpect(jsonPath("$..message").value("???????????? 2??? ~ 20??? ????????? ??????????????????"))
        .andExpect(jsonPath("$.[?(@.field == 'oldPassword')]").doesNotExist())
        .andExpect(jsonPath("$.[?(@.field == 'newPassword')]").doesNotExist())
        .andDo(print());
    }


    @Test
    @DisplayName("?????? - ????????? ?????? ??????")
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
        .andExpect(jsonPath("$..message").value("??????????????? ??????????????????"))
        .andExpect(jsonPath("$.[?(@.field == 'oldPassword')]").doesNotExist())
        .andExpect(jsonPath("$.[?(@.field == 'newPassword')]").doesNotExist())
        .andDo(print());
    }


    @Test
    @DisplayName("?????? - ????????? ??????")
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
        .andExpect(jsonPath("$..message").value("???????????? ??????????????????"))
        .andExpect(jsonPath("$.[?(@.field == 'oldPassword')]").doesNotExist())
        .andExpect(jsonPath("$.[?(@.field == 'newPassword')]").doesNotExist())
        .andDo(print());
    }

    @Test
    @DisplayName("?????? - ????????? ????????? ??????")
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
        .andExpect(jsonPath("$..message").value("?????? ???????????? ????????????"))
        .andExpect(jsonPath("$.[?(@.field == 'oldPassword')]").doesNotExist())
        .andExpect(jsonPath("$.[?(@.field == 'newPassword')]").doesNotExist())
        .andDo(print());
    }


    @Test
    @DisplayName("?????? - ????????? ?????? ??????")
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
    @DisplayName("?????? - ?????? ???????????? ?????? ??????")
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
            .andExpect(jsonPath("$..message").value("?????? ??????????????? ???????????? ????????????"))
        .andExpect(jsonPath("$.[?(@.field == 'newPassword')]").doesNotExist())
        .andDo(print());
    }

    @Test
    @DisplayName("?????? - ?????? ???????????? ??????, ??? ???????????? ????????? ??????")
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
                .andExpect(jsonPath("$..message").value("??????????????? 2??? ~ 20??? ????????? ??????????????????"))
        .andDo(print());
    }

    @Test
    @DisplayName("?????? - ?????? ???????????? ??????, ??? ???????????? ?????? ??????????????? ??????")
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
                .andExpect(jsonPath("$..message").value("?????? ??????????????? ????????????"))
        .andDo(print());
    }

    @Test
    @DisplayName("?????? - ???????????? ?????? ??????")
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