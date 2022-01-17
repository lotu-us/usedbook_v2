package team.hello.usedbook.controller.post;

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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import team.hello.usedbook.config.SessionConstants;
import team.hello.usedbook.domain.Member;
import team.hello.usedbook.repository.MemberRepository;
import team.hello.usedbook.repository.PostRepository;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class listTest {

    @Autowired private MockMvc mock;
    @Autowired private WebApplicationContext ctx;
    @Autowired private ObjectMapper objectMapper;
    @MockBean  private MockHttpSession session;
    @Autowired private MemberRepository memberRepository;
    @MockBean private PostRepository postRepository;

    private Member member;

    @BeforeEach
    public void setUp() throws IOException {
        objectMapper = Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .modules(new JavaTimeModule())
                .build();

        mock = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();

        member = new Member("1212@1212", "1212", "1212");
        memberRepository.save(member);
        member.addIdForTest(2L);

        session = new MockHttpSession();
        session.setAttribute(SessionConstants.LOGIN_MEMBER, member);

        //given
        when(postRepository.findAllCount(any())).thenReturn(100);
        when(postRepository.findAll(any())).thenReturn(null);

    }


    private ResultActions getList(String uri, String page, String srange, String stext, String otext, String otype) throws Exception {
        ResultActions perform = mock.perform(
                get(uri).session(session)
                .param("page", page)
                .param("srange", srange)
                .param("stext", stext)
                .param("otext", otext)
                .param("otype", otype)
                .accept(MediaType.APPLICATION_JSON)
        );
        return perform;
    }


    @Test
    @DisplayName("성공 - 통합검색 1페이지")
    void list_all_page_1() throws Exception {
        //when //then
        getList("/api/posts", "", "", "", "", "")
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.pagination.page").value(1))
        .andExpect(jsonPath("$.pagination.startPage").value(1))
        .andExpect(jsonPath("$.pagination.endPage").value(5))
        .andDo(print());
    }


    @Test
    @DisplayName("성공 - 통합검색 13페이지")
    void list_all_page_13() throws Exception {
        //when //then
        getList("/api/posts", "13", "", "", "", "")
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.pagination.page").value(13))
        .andExpect(jsonPath("$.pagination.startPage").value(11))
        .andExpect(jsonPath("$.pagination.endPage").value(15))
        .andDo(print());
    }

    @Test
    @DisplayName("성공 - 페이지가 limitpage의 배수")
    void list_all_page_10() throws Exception {
        //when //then
        getList("/api/posts", "10", "", "", "", "")
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.pagination.page").value(10))
        .andExpect(jsonPath("$.pagination.startPage").value(6))
        .andExpect(jsonPath("$.pagination.endPage").value(10))
        .andDo(print());
    }

    @Test
    @DisplayName("성공 - 페이지 0일 경우엔 1로 변경처리")
    void list_all_page_0() throws Exception {
        //when //then
        getList("/api/posts", "0", "", "", "", "")
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.pagination.page").value(1))
        .andDo(print());
    }

    @Test
    @DisplayName("성공 - 페이지 endPage보다 클 경우엔 endPage로 변경처리")
    void list_all_page_endPage() throws Exception {
        //when //then
        getList("/api/posts", "1000", "", "", "", "")
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.pagination.page").value(100/5))
        .andDo(print());
    }




}