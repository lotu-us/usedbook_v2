package team.hello.usedbook.controller.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.hello.usedbook.domain.Post;
import team.hello.usedbook.domain.dto.Pagination;
import team.hello.usedbook.domain.dto.PostDTO;
import team.hello.usedbook.repository.PostFileRepository;
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
    @Autowired private PostFileRepository postFileRepository;

    @PostMapping("/post")
    public ResponseEntity writePost(@Validated @RequestPart(value = "jsonData") PostDTO editForm, BindingResult bindingResult,
                                    @RequestPart(value = "fileList") List<MultipartFile> fileList, HttpSession session){
        //@Validated @RequestBody PostDTO editForm, BindingResult bindingResult, HttpSession session){
        // Resolved [org.springframework.web.HttpMediaTypeNotSupportedException: Content type 'multipart/form-data;boundary

        List<ValidResultList.ValidResult> validResults = postService.postSaveCheck(editForm, fileList, bindingResult);
        if(bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validResults);
        }

        Long postId = postService.postSave(session, editForm);
        postService.postFileSave(postId, fileList);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    //@PutMapping("/post/{postId}")
    /*
    PUT은 특정 리소스를 갱신하는 역할을 하는데 multipart로 보내면 한번에 여러 리소스를 처리하므로 이미지 같은 경우를 PUT으로 처리하려면 이미지등에 대한 리소스 URI에
    별도의 PUT 요청을 보내서 갱신하고 일반적인 폼은 따로 처리하라는 의미이다. 스펙에 빠삭하지 못해서 정확치는 않지만 이 경우에는 한 URI로 PUT을 보내서
    여러 리소스(회원 정보 + 이미지)를 한꺼번에 처리하려고 했으므로 PUT이 적합치 않다는 의미로 보인다.
    * */
    @PostMapping("/post/{postId}")
    public ResponseEntity updatePost(@PathVariable Long postId,
                                     @Validated @RequestPart(value = "jsonData") PostDTO editForm, BindingResult bindingResult,
                                     @RequestPart(value = "fileList", required = false) List<MultipartFile> fileList,
                                     @RequestPart(value = "removeFileList", required = false) List<String> removeFileList){

        List<ValidResultList.ValidResult> validResults = postService.postSaveCheck(editForm, fileList, bindingResult);
        if(bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validResults);
        }

        postService.postUpdate(postId, editForm);
        postService.postFileUpdate(postId, fileList, removeFileList);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity detailPost(@PathVariable Long postId){

        Post post = postRepository.findById(postId);
        if(post == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 접근입니다.");
        }

        Map<String, Object> result = postService.detail(postId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    @DeleteMapping("/post/{postId}")
    public ResponseEntity deletePost(@PathVariable Long postId){

        postRepository.deleteById(postId);
        //postFileRepository는 db cascade 설정되어있음
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
