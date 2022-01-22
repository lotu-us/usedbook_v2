package team.hello.usedbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import team.hello.usedbook.config.FileConstants;
import team.hello.usedbook.config.SessionConstants;
import team.hello.usedbook.domain.Member;
import team.hello.usedbook.domain.Post;
import team.hello.usedbook.domain.PostFile;
import team.hello.usedbook.domain.PostLike;
import team.hello.usedbook.domain.dto.Pagination;
import team.hello.usedbook.domain.dto.PostDTO;
import team.hello.usedbook.domain.enums.Category;
import team.hello.usedbook.domain.enums.SaleStatus;
import team.hello.usedbook.repository.PostFileRepository;
import team.hello.usedbook.repository.PostLikeRepository;
import team.hello.usedbook.repository.PostRepository;
import team.hello.usedbook.utils.ValidResultList;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

import static team.hello.usedbook.domain.dto.PostDTO.Response.ListPostToListDto;

@Service
public class PostService {
    @Autowired private PostRepository postRepository;
    @Autowired private PostFileRepository postFileRepository;
    @Autowired private PostLikeRepository postLikeRepository;


    public List<ValidResultList.ValidResult> postSaveCheck(PostDTO.EditForm editForm, List<MultipartFile> fileList, BindingResult bindingResult) {
        if(fileList != null){
            if(fileList.size() == 0){
                bindingResult.rejectValue("fileList", "emptyFile", "이미지는 최소 1개 이상 있어야합니다.");
            }
        }

        try{
            Category category = Category.valueOf(editForm.getCategory());
        }catch(IllegalArgumentException e){
            bindingResult.rejectValue("category", "notExistCategory", "없는 카테고리 입니다.");
        }

        List<ValidResultList.ValidResult> validResults = new ValidResultList(bindingResult).getList();
        return validResults;
    }


    public void postSave(HttpSession session, PostDTO.EditForm editForm, List<MultipartFile> fileList) {
        Member loginMember = (Member) session.getAttribute(SessionConstants.LOGIN_MEMBER);
        String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        Post post = new Post(
                loginMember.getNickname(),
                editForm.getTitle(),
                editForm.getContent(),
                editForm.getPrice(),
                editForm.getStock(),
                Category.valueOf(editForm.getCategory()),
                createTime
        );

        postRepository.save(post);
        fileSave(post.getId(), fileList, FileConstants.userUploadImgPath);
    }



    public void postUpdate(Long postId, PostDTO.EditForm editForm, List<MultipartFile> fileList, List<String> removeFileList) {
        Post post = new Post(
                editForm.getTitle(),
                editForm.getContent(),
                editForm.getPrice(),
                editForm.getStock(),
                SaleStatus.valueOf(editForm.getSaleStatus()),
                Category.valueOf(editForm.getCategory())
        );
        postRepository.update(postId, post);

        if(removeFileList != null){
            fileRemove(postId, removeFileList, FileConstants.userUploadImgPath);
        }
        if(fileList != null){   //파일을 추가로 업로드한 것이 있을 때
            fileSave(postId, fileList, FileConstants.userUploadImgPath);
        }

    }

    public PostDTO.Response detail(Long postId, HttpSession session) {
        //조회수 업데이트
        addViewCount(postId);

        PostDTO.Response post = postRepository.findPostAndFileById(postId);

        //조회하는 자가 회원일 때, 게시글에 관심버튼 눌렀으면 누른상태로 보여주어야함
        Member loginMember = (Member) session.getAttribute(SessionConstants.LOGIN_MEMBER);
        if(loginMember != null){    //회원일 때

            PostLike postLike = postLikeRepository.find(loginMember.getId(), postId);
            if(postLike != null){   //관심상품으로 등록했을때
                post.setLikeStatus(true);
            }

            //조회하는 자가 회원이고, 게시글 작성자이면 수정 삭제버튼 보이게
            if(loginMember.getNickname().equals(post.getWriter())){
                post.setMenuStatus(true);
            }
        }

        return post;
    }


    public Map<String, Object> list(String category, Pagination pagination) {
        pagination.setCategory(category);
        int categoryAndSearchCount = postRepository.findAllCount(pagination);

        pagination.init(categoryAndSearchCount);
        List<Post> posts = postRepository.findAll(pagination);
        List<PostDTO.Response> responses = ListPostToListDto(posts);

        Map<String, Object> result = new HashMap<>();
        result.put("posts", responses);
        result.put("pagination", pagination);

        return result;
    }



    public void addCommentCount(Long postId) {
        postRepository.addCommentCount(postId);
    }

    private void addViewCount(Long postId) {
        postRepository.addViewCount(postId);
    }

    private void fileSave(Long postId, List<MultipartFile> fileList, String uploadPath){
        for (MultipartFile multipartFile : fileList) {
            UUID uuid = UUID.randomUUID();
            String filename = uuid + "_" + multipartFile.getOriginalFilename();
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
        }
    }

    private void fileRemove(Long postId, List<String> removeFileList, String uploadPath) {
        for (String removeFileName : removeFileList) {
            String fileName = URLDecoder.decode(removeFileName, StandardCharsets.UTF_8);
            postFileRepository.removeFile(postId, fileName);

            File file = new File(uploadPath + File.separator + removeFileName);

            if(file.exists()){
                file.delete();
            }
        }
    }


    @Transactional
    public int changePostLike(Long postId, String status, Member loginMember) {
        int updated=0;
        PostLike postLike = new PostLike(loginMember.getId(), postId);
        if(status.equals("true")){
            postLikeRepository.add(postLike);
            updated = postRepository.changeLikeCount(postId, "+1");
        }
        else{
            postLikeRepository.remove(postLike);
            updated = postRepository.changeLikeCount(postId, "-1");
        }

        return updated;
    }
}
