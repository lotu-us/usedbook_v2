package team.hello.usedbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import team.hello.usedbook.config.FileConstants;
import team.hello.usedbook.config.SessionConstants;
import team.hello.usedbook.domain.Member;
import team.hello.usedbook.domain.Post;
import team.hello.usedbook.domain.PostFile;
import team.hello.usedbook.domain.dto.Pagination;
import team.hello.usedbook.domain.dto.PostDTO;
import team.hello.usedbook.domain.enums.Category;
import team.hello.usedbook.domain.enums.SaleStatus;
import team.hello.usedbook.repository.PostFileRepository;
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

    public PostDTO.Response detail(Long postId) {
        //조회수 업데이트
        addViewCount(postId);

        PostDTO.Response post = postRepository.findPostAndFileById(postId);
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

    public Map<String, Object> allCategoryListForIndex(int count) {
        Map<String, Object> result = new HashMap<>();

        Category[] values = Category.values();
        for (Category value : values) {

            String lowerCategory = value.toString().toLowerCase();
            List<PostDTO.Response> allForIndex = postRepository.findAllForIndex(lowerCategory, count);

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


    public Map<String, Object> dashboardGetMyPosts(HttpSession session, Pagination pagination) {
        Member loginMember = (Member) session.getAttribute(SessionConstants.LOGIN_MEMBER);

        pagination.setCategory(null);
        int count = postRepository.findAllForDashboardCount(loginMember);

        pagination.init(count);
        List<Post> posts = postRepository.findAllForDashboard(loginMember, pagination);

        Map<String, Object> result = new HashMap<>();

        List<PostDTO.Response> responses = ListPostToListDto(posts);
        result.put("posts", responses);
        result.put("pagination", pagination);

        return result;
    }


    public void addCommentCount(Long postId) {
        postRepository.addCommentCount(postId);
    }

    public int addLikeCount(Long postId, String status) {
        String num;
        if(status.equals("true")){ num = "+1"; }
        else{                      num = "-1"; }
        int updated = postRepository.changeLikeCount(postId, num);
        return updated;
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




}
