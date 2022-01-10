package team.hello.usedbook.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    //요청이 들어오고 컨트롤러 실행 전에 실행됨
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        HttpSession session = request.getSession(false);
        //세션이 있다면 해당 세션을 반환, 세션이 없다면 null리턴
        //true는 default값. 세션이 있다면 해당 세션을 반환, 세션이 없다면 새로운 세션을 생성성

       if(session == null || session.getAttribute(SessionConstants.LOGIN_MEMBER) == null){
            response.sendRedirect("/login?redirectURL="+requestURI);
            //기존 요청을 쿼리 파라미터로 redirectURL로 지정함으로써 로그인한 이후에는 기존 요청 페이지로 리다이렉트 될 수 있도록 처리하는 것이 고객 입장에서 편리
            //redirect하면 get 요청이 이루어진다. 따라서 post에서 사용하고싶다면 반드시 login.html의 form action에 매개변수를 붙여주어야 한다.
            return false;
        }

        return true;
    }

    /*
    1. DispatcherServlet은 Controller를 실행해줄 HandlerAdapter를 찾는다.
    2. 이 때, Adapter를 찾고 handle을 실행하기 위해 필요한 파라미터를 생성하기 위해 Resolver는 실행된다.
    3. HandlerAdapter는 Controller를 실행한다.
    4. 이 때, Interceptor의 postHandle이 실행된다.
    5. DispatcherServlet은 실행한 결과를 ViewResolver에게 전달한다.

    posthandle에 세션을 찾아 반환하는 로직을 작성하면 컨트롤러가 다 끝나버린 후에 실행되니 세션값 조작 불가능!!
    postHandle보다 먼저 실행되는 resolver를 사용하자(webconfig파일)
    */
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
//    }

}
