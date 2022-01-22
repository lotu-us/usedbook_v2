package team.hello.usedbook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import team.hello.usedbook.domain.dto.PostDTO;
import team.hello.usedbook.domain.enums.Category;
import team.hello.usedbook.repository.IndexRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired private IndexRepository indexRepository;

    @GetMapping("/")
    public String index(){
        return "index";
    }


    @GetMapping("/api/posts/allCategoryListForIndex")
    public ResponseEntity allCategoryListForIndex(@RequestParam int count){

        Map<String, Object> result = allCategoryListForIndexMethod(count);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    private Map<String, Object> allCategoryListForIndexMethod(int count) {
        Map<String, Object> result = new HashMap<>();

        Category[] values = Category.values();
        for (Category value : values) {

            String lowerCategory = value.toString().toLowerCase();
            List<PostDTO.Response> allForIndex = indexRepository.findAllForIndex(lowerCategory, count);

            for (PostDTO.Response forIndex : allForIndex) {
                List<String> fileNames = forIndex.getFileNames();
                if(fileNames.size() != 0){  //테스트로 파일 저장안한것들 오류발생안하게..
                    String first = fileNames.get(0);
                    fileNames.clear();
                    fileNames.add(first);
                }
            }
            result.put(lowerCategory, allForIndex);
        }

        return result;
    }
}
