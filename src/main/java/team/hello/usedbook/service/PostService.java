package team.hello.usedbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.hello.usedbook.config.SessionConstants;
import team.hello.usedbook.domain.Member;
import team.hello.usedbook.domain.Post;
import team.hello.usedbook.domain.PostFile;
import team.hello.usedbook.domain.dto.PostDTO;
import team.hello.usedbook.repository.PostFileRepository;
import team.hello.usedbook.repository.PostRepository;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class PostService {
    @Autowired private PostRepository postRepository;
    @Autowired private PostFileRepository postFileRepository;

    public Long postSave(HttpSession session, PostDTO.EditForm editForm) {
        Member loginMember = (Member) session.getAttribute(SessionConstants.LOGIN_MEMBER);
        String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        Post post = new Post(
                loginMember.getEmail(),
                editForm.getTitle(),
                editForm.getContent(),
                editForm.getPrice(),
                editForm.getStock(),
                editForm.getCategory(),
                createTime
        );

        postRepository.save(post);
        return post.getId();
    }

    public void postFileSave(Long postId, PostDTO.EditForm editForm) {
        String uploadPath = Paths.get("D:", "projectEn", "usedbook2", "userUploadImg").toString();

        int order = 10;
        for (MultipartFile multipartFile : editForm.getFileList()) {
            UUID uuid = UUID.randomUUID();
            String filename = uuid + "_" + order + "_" + multipartFile.getOriginalFilename();
            Path savePath = Paths.get(uploadPath + File.separator + filename).toAbsolutePath();

            try {
                multipartFile.transferTo(savePath.toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
            //MultipartFile.transferTo()는 요청 시점의 임시 파일을 로컬 파일 시스템에 영구적으로 복사하는 역할을 수행한다.
            // 단 한번만 실행되며 두번째 실행부터는 성공을 보장할 수 없다.
            //Embedded Tomcat을 컨테이너로 사용할 경우 DiskFileItem.write()가 실제 역할을 수행한다.
            // I/O 사용을 최소화하기 위해 파일 이동을 시도하며, 이동이 불가능할 경우 파일 복사를 진행한다.

            PostFile postFile = new PostFile(
                    postId,
                    uploadPath,
                    filename
            );

            postFileRepository.save(postFile);
            order--;
        }
    }
}
