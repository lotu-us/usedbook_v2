package team.hello.usedbook.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import team.hello.usedbook.config.FileConstants;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/api")
public class FileUrlApiController {

    @GetMapping("/image/{imageName}")
    public ResponseEntity<Resource> fileUrl(@PathVariable String imageName) throws IOException {
        String uploadPath = FileConstants.userUploadImgPath;

        Resource resource = new FileSystemResource(uploadPath + File.separator + imageName);

        if(!resource.exists()){
            return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
        }

        HttpHeaders headers = new HttpHeaders();
        Path savePath = Paths.get(uploadPath + File.separator + imageName).toAbsolutePath();
        headers.add("Content-Type", Files.probeContentType(savePath));

        return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
    }

}
