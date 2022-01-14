package team.hello.usedbook.controller.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import team.hello.usedbook.domain.dto.PostDTO;
import team.hello.usedbook.service.PostService;
import team.hello.usedbook.utils.ValidResultList;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class PostApiController {

    @Autowired private PostService postService;

    @PostMapping("/api/post")
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
}
