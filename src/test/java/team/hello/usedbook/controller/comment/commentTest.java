package team.hello.usedbook.controller.comment;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import team.hello.usedbook.config.SessionConstants;
import team.hello.usedbook.domain.Comment;
import team.hello.usedbook.domain.Member;
import team.hello.usedbook.domain.dto.CommentDTO;
import team.hello.usedbook.repository.CommentRepository;
import team.hello.usedbook.repository.MemberRepository;

import java.io.IOException;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class commentTest {

    @Autowired private MockMvc mock;
    @Autowired private WebApplicationContext ctx;
    @Autowired private ObjectMapper objectMapper;
    @MockBean  private MockHttpSession session;
    @Autowired private MemberRepository memberRepository;
    @Autowired private CommentRepository commentRepository;

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

    }

    private String createForm(Long postId, String content, Long parentId, int depth) throws Exception{
        CommentDTO.EditForm commentDTO = new CommentDTO.EditForm();
        commentDTO.setPostId(postId);
        commentDTO.setContent(content);
        commentDTO.setParentId(parentId);
        commentDTO.setDepth(depth);
        return objectMapper.writeValueAsString(commentDTO);
    }


    @Test
    @DisplayName("성공 - 댓글")
    @Transactional  //테스트 끝난 후 자동 rollback
    void writeCommentSuccess() throws Exception {
        //given
        String form = createForm(21L, "dlfjwlf", 0L, 0);

        //when //then
        mock.perform(
                MockMvcRequestBuilders.post("/api/comment/21").content(form).session(session)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.postId").value(21))
        .andExpect(jsonPath("$.content").value("dlfjwlf"))
        .andExpect(jsonPath("$.parentId").value(0))
        .andExpect(jsonPath("$.depth").value(0))
        .andDo(print());
    }


    @Test
    @DisplayName("성공 - 답글")
    @Transactional  //테스트 끝난 후 자동 rollback
    void writeCommentSuccessReply() throws Exception {
        //given
        String form = createForm(21L, "dlfjwlf", 3L, 1);

        //when //then
        mock.perform(
                MockMvcRequestBuilders.post("/api/comment/21").content(form).session(session)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.postId").value(21))
        .andExpect(jsonPath("$.content").value("dlfjwlf"))
        .andExpect(jsonPath("$.parentId").value(3))
        .andExpect(jsonPath("$.depth").value(1))
        .andDo(print());
    }

    @Test
    @DisplayName("성공 - 대댓글")
    @Transactional  //테스트 끝난 후 자동 rollback
    void writeCommentSuccessNestedReply() throws Exception {
        //given
        String form = createForm(21L, "dlfjwlf", 3L, 2);

        //when //then
        mock.perform(
                MockMvcRequestBuilders.post("/api/comment/21").content(form).session(session)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.postId").value(21))
        .andExpect(jsonPath("$.content").value("dlfjwlf"))
        .andExpect(jsonPath("$.parentId").value(3))
        .andExpect(jsonPath("$.depth").value(2))
        .andDo(print());
    }

    @Test
    @DisplayName("실패 - postid null, content null, depth < 0")
    @Transactional  //테스트 끝난 후 자동 rollback
    void writeCommentFail() throws Exception {
        //given
        String form = createForm(null, "", null, -1);

        //when //then
        mock.perform(
                MockMvcRequestBuilders.post("/api/comment/21").content(form).session(session)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.[?(@.field == 'content')]message").value("댓글 내용을 입력해주세요"))
        .andExpect(jsonPath("$.[?(@.field == 'postId')]message").value("글 번호를 입력해주세요"))
        .andExpect(jsonPath("$.[?(@.field == 'depth')]message").value("댓글의 깊이는 0부터 입니다."))
        .andDo(print());
    }

    @Test
    @DisplayName("실패 - postid null, content null, depth > 2")
    @Transactional  //테스트 끝난 후 자동 rollback
    void writeCommentFail2() throws Exception {
        //given
        String form = createForm(null, "", null, 3);

        //when //then
        mock.perform(
                MockMvcRequestBuilders.post("/api/comment/21").content(form).session(session)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.[?(@.field == 'content')]message").value("댓글 내용을 입력해주세요"))
        .andExpect(jsonPath("$.[?(@.field == 'postId')]message").value("글 번호를 입력해주세요"))
        .andExpect(jsonPath("$.[?(@.field == 'depth')]message").value("댓글의 깊이는 2까지 입니다."))
        .andDo(print());
    }

    @Test
    @DisplayName("성공 - 수정")
    @Transactional  //테스트 끝난 후 자동 rollback
    void updateCommentSuccess() throws Exception {
        //given
        Comment comment = new Comment(21L, "1212" ,"수정 전 내용", 0L, 0, "2022-01-18 12:46:14");
        commentRepository.save(comment);

        String form = "{\"content\" : \"수정 후 내용\"}";

        //when //then
        mock.perform(
                MockMvcRequestBuilders.put("/api/comment/21/"+comment.getId()).content(form).session(session)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(content().string("1"))
        .andDo(print());
    }

    @Test
    @DisplayName("성공 - 삭제")
    @Transactional  //테스트 끝난 후 자동 rollback
    void deleteCommentSuccess() throws Exception {
        //given
        Comment comment = new Comment(21L, "1212" ,"수정 전 내용", 0L, 0, "2022-01-18 12:46:14");
        commentRepository.save(comment);

        //when //then
        mock.perform(
                MockMvcRequestBuilders.delete("/api/comment/21/"+comment.getId()).session(session)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(content().string("1"))
        .andDo(print());
    }

}