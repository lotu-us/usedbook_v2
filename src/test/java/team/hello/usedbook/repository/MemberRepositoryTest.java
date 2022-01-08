package team.hello.usedbook.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import team.hello.usedbook.domain.Member;

import java.util.List;

// @ExtendWith(SpringExtension.class)   // JUnit5 사용 시 작성, MybatisTest 2.0.1버전 이상에서 생략 가능
// @RunWith(SpringRunner.class)         // JUnit4 사용 시 작성
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 실 데이터베이스에 연결 시 필요한 어노테이션
public class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Test
    void findAll() {
        List<Member> all = memberRepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(1);
    }

    @Test
    void findAllXml() {
        List<Member> all = memberRepository.findAllXml();
        Assertions.assertThat(all.size()).isEqualTo(1);
    }

    @Test
    void findByEmail(){
        Member byEmail = memberRepository.findByEmail("11@11");
        Assertions.assertThat("11@11").isEqualTo(byEmail.getEmail());
        Assertions.assertThat("11").isEqualTo(byEmail.getPassword());
    }
}