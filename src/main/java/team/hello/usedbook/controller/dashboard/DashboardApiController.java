package team.hello.usedbook.controller.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import team.hello.usedbook.domain.dto.Pagination;
import team.hello.usedbook.service.DashboardService;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class DashboardApiController {
    @Autowired private DashboardService dashboardService;

    @GetMapping("/dashboard/myPosts")
    public ResponseEntity myPosts(HttpSession session, @ModelAttribute Pagination pagination){

        Map<String, Object> result = dashboardService.findMyPosts(session, pagination);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/dashboard/myComments")
    public ResponseEntity myComments(HttpSession session, @ModelAttribute Pagination pagination){

        Map<String, Object> result = dashboardService.findMyComments(session, pagination);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/dashboard/myFavorites")
    public ResponseEntity myFavorites(HttpSession session, @ModelAttribute Pagination pagination){

        Map<String, Object> result = dashboardService.findMyFavorites(session, pagination);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
