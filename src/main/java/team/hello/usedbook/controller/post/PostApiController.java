package team.hello.usedbook.controller.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.hello.usedbook.domain.Pagination;
import team.hello.usedbook.domain.dto.PostDTO;
import team.hello.usedbook.repository.PostRepository;
import team.hello.usedbook.service.PostService;
import team.hello.usedbook.utils.ValidResultList;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class PostApiController {

    @Autowired private PostService postService;
    @Autowired private PostRepository postRepository;

    @PostMapping("/post")
    public ResponseEntity writePost(@Validated @RequestPart(value = "jsonData") PostDTO.EditForm editForm, BindingResult bindingResult,
                                    @RequestPart(value = "fileList") List<MultipartFile> fileList, HttpSession session){
        //@Validated @RequestBody PostDTO.EditForm editForm, BindingResult bindingResult, HttpSession session){
        // Resolved [org.springframework.web.HttpMediaTypeNotSupportedException: Content type 'multipart/form-data;boundary

        List<ValidResultList.ValidResult> validResults = postService.postSaveCheck(editForm, fileList, bindingResult);
        if(bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validResults);
        }

        Long postId = postService.postSave(session, editForm);
        postService.postFileSave(postId, fileList);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    /*
    modelattribute
    클라이언트가 전송하는 HTTP parameter(URL 끝에 추가하는 파라미터), HTTP Body 내용을 Setter 함수를 통해 1:1로 객체에 데이터를 바인딩합니다. (Setter 필수!)
    HTTP Body 내용은 multipart/form-data 형태 입니다.

    requestbody
    클라이언트가 body에 application/json 형태로 값(보통 객체)을 담아 전송하면, body의 내용을 다시 Java Object(객체)로 변환해주는 역할을 수행합니다.
    body에 담은 값을 변환하기 때문에, GET 이 아닌 POST 방식에서만 사용이 가능합니다. (GET 방식은 Header에 값을 담아서 보냅니다.)

    따라서 modelattribute를 사용한다
   * */
    @GetMapping({"/posts", "/posts/{category}"})
    public ResponseEntity list(@PathVariable(required = false) String category, @ModelAttribute Pagination pagination){

        //category lowercase임
        Map<String, Object> result = postService.list(category, pagination);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


}
