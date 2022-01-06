package team.hello.usedbook.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import team.hello.usedbook.domain.Member;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// @ExtendWith(SpringExtension.class)   // JUnit5 사용 시 작성, MybatisTest 2.0.1버전 이상에서 생략 가능
// @RunWith(SpringRunner.class)         // JUnit4 사용 시 작성
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 실 데이터베이스에 연결 시 필요한 어노테이션
public class MemberMapperTest {
    @Autowired MemberMapper memberMapper;

    @Test
    void findAll() {
        List<Member> all = memberMapper.findAll();
        Assertions.assertThat(all.size()).isEqualTo(1);
    }

    @Test
    void findAllXml() {
        List<Member> all = memberMapper.findAllXml();
        Assertions.assertThat(all.size()).isEqualTo(1);
    }
}