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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import team.hello.usedbook.config.SessionConstants;
import team.hello.usedbook.domain.Member;
import team.hello.usedbook.domain.dto.PostDTO;
import team.hello.usedbook.domain.enums.Category;
import team.hello.usedbook.domain.enums.SaleStatus;
import team.hello.usedbook.repository.MemberRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class writeTest {

    @Autowired private MockMvc mock;
    @Autowired private WebApplicationContext ctx;
    @Autowired private ObjectMapper objectMapper;
    @MockBean  private MockHttpSession session;
    @Autowired private MemberRepository memberRepository;

    private Member member;
    private MockMultipartFile file;
    private List<MockMultipartFile> fileList;

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

        file = new MockMultipartFile("fileList", "file.jpg", MediaType.IMAGE_JPEG_VALUE, "<<pdf data>>".getBytes(StandardCharsets.UTF_8));
    }

    String uri = "/api/post";

    private MockMultipartFile createForm(String title, String category, Integer price, Integer stock, String saleStatus, String content) throws Exception{
        PostDTO.EditForm editForm = new PostDTO.EditForm(title, category, price, stock, saleStatus, content);
        String form = objectMapper.writeValueAsString(editForm);
        MockMultipartFile jsonData = new MockMultipartFile("jsonData", "jsonData", MediaType.APPLICATION_JSON_VALUE, form.getBytes(StandardCharsets.UTF_8));
        return jsonData;
    }


    @Test
    @DisplayName("성공")
    @Transactional  //테스트 끝난 후 자동 rollback
    void writePostSuccess() throws Exception {
        //given
        MockMultipartFile jsonData = createForm("제목", Category.HUMANITIES.toString(), 2200, 10, SaleStatus.READY.toString(), "내용");

        //when //then
        mock.perform(
                multipart(uri)
                .file(file).file(jsonData).session(session)
                .contentType(MediaType.MULTIPART_FORM_DATA).accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[?(@.field == 'title')]").doesNotExist())
        .andExpect(jsonPath("$.[?(@.field == 'category')]").doesNotExist())
        .andExpect(jsonPath("$.[?(@.field == 'price')]").doesNotExist())
        .andExpect(jsonPath("$.[?(@.field == 'stock')]").doesNotExist())
        .andExpect(jsonPath("$.[?(@.field == 'content')]").doesNotExist())
        .andExpect(jsonPath("$.[?(@.field == 'fileList')]").doesNotExist())
        .andDo(print());
    }


    @Test
    @DisplayName("실패 - 내용 모두 빈칸, 파일 있음")
    @Transactional  //테스트 끝난 후 자동 rollback
    void writeFail_empty_all_except_file() throws Exception {
        //given
        MockMultipartFile jsonData = createForm("", "", null, null, SaleStatus.READY.toString(), "");

        //when //then
        mock.perform(
                multipart(uri)
                .file(file).file(jsonData).session(session)
                .contentType(MediaType.MULTIPART_FORM_DATA).accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.[?(@.field == 'title')]").exists())
        .andExpect(jsonPath("$.[?(@.field == 'category')]").exists())
        .andExpect(jsonPath("$.[?(@.field == 'price')]").exists())
        .andExpect(jsonPath("$.[?(@.field == 'stock')]").exists())
        .andExpect(jsonPath("$.[?(@.field == 'content')]").exists())
        .andExpect(jsonPath("$.[?(@.field == 'fileList')]").doesNotExist())
        .andDo(print());
    }

    @Test
    @DisplayName("실패 - 내용 모두 빈칸, 파일 없음")
    @Transactional  //테스트 끝난 후 자동 rollback
    void writeFail_empty_all() throws Exception {
        //given
        MockMultipartFile jsonData = createForm("", "", null, null, SaleStatus.READY.toString(), "");

        //when //then
        mock.perform(
                multipart(uri)
                .file(jsonData).session(session)
                .contentType(MediaType.MULTIPART_FORM_DATA).accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest())     //Required request part 'fileList' is not present
        .andDo(print());
    }

    @Test
    @DisplayName("실패 - 내용 검증 오류, 파일 있음")
    @Transactional  //테스트 끝난 후 자동 rollback
    void writeFail_valid() throws Exception {
        //given
        MockMultipartFile jsonData = createForm("12테스트test!@", "ㅎㅎ", 10, 101, SaleStatus.READY.toString(), "12테스트test!@");

        //when //then
        mock.perform(
                multipart(uri)
                .file(file).file(jsonData).session(session)
                .contentType(MediaType.MULTIPART_FORM_DATA).accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.[?(@.field == 'title')]").doesNotExist())

        .andExpect(jsonPath("$.[?(@.field == 'category')]").exists())
                .andExpect(jsonPath("$.[?(@.field == 'category')]message").value("없는 카테고리 입니다."))

        .andExpect(jsonPath("$.[?(@.field == 'price')]").exists())
                .andExpect(jsonPath("$.[?(@.field == 'price')]message").value("가격을 1000원 이상으로 설정해주세요"))

        .andExpect(jsonPath("$.[?(@.field == 'stock')]").exists())
                .andExpect(jsonPath("$.[?(@.field == 'stock')]message").value("수량은 100개까지 설정할 수 있습니다"))

        .andExpect(jsonPath("$.[?(@.field == 'content')]").doesNotExist())
        .andExpect(jsonPath("$.[?(@.field == 'fileList')]").doesNotExist())
        .andDo(print());
    }


}