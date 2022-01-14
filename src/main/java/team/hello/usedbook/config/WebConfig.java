package team.hello.usedbook.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final String[] whitelist = {"/css/**", "/js/**", "/img/**", "/",
            "/*", "/post/**", "/api/**",
            "/swagger-ui/**", "/swagger-resources/**", "/swagger-ui.html", "/v2/api-docs", "/webjars/**",
            "/mybatis",
            "/error"
    };
    //whitelist 같은 경우 로그인하지 않아도 접근할 수 있는 자원들 목록

    //빈으로 등록된 LoginInterceptor를 주입하여 동작하게한다
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .order(1)                           //낮을수록 먼저 호출됨. 인터셉터 체인 순서
                .addPathPatterns("/**")             //모든 요청에 대해 인터셉터 실행
                .excludePathPatterns(whitelist);    //인터셉터에서 제외할 패턴(URL) 지정
    }


    /*
    1. DispatcherServlet은 Controller를 실행해줄 HandlerAdapter를 찾는다.
    2. 이 때, Adapter를 찾고 handle을 실행하기 위해 필요한 파라미터를 생성하기 위해 Resolver는 실행된다.
    3. HandlerAdapter는 Controller를 실행한다.
    4. 이 때, Interceptor의 postHandle이 실행된다.
    5. DispatcherServlet은 실행한 결과를 ViewResolver에게 전달한다.

    */
    //컨트롤러에 메서드 인자값으로 임의의 값을 전달하려할때 사용함
    //https://stove99.github.io/java/2019/05/03/spring-boot-argument-resolver/ 참고
//    @Override
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
//        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
//    }
}
