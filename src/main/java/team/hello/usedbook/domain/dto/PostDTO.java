package team.hello.usedbook.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.hello.usedbook.domain.Post;
import team.hello.usedbook.domain.enums.Category;
import team.hello.usedbook.domain.enums.SaleStatus;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;


public class PostDTO {

    @Data
    @AllArgsConstructor
    public static class EditForm{
        @NotBlank(message = "제목을 입력해주세요")
        private String title;

        @NotBlank(message = "카테고리를 선택해주세요")
        private String category;

        @NotNull(message = "값을 입력해주세요")
        @Min(value = 1000, message = "가격을 1000원 이상으로 설정해주세요")
        private Integer price;

        @NotNull(message = "값을 입력해주세요")
        @Max(value = 100, message = "수량은 100개까지 설정할 수 있습니다")
        private Integer stock;

        @NotBlank(message = "내용을 입력해주세요")
        private String content;

        @NotBlank(message = "판매상태를 선택해주세요")
        private String saleStatus;
    }

    @Data
    @NoArgsConstructor
    public static class Response{
        private Long id;
        private String writer;  //member테이블의 email참조 중. update시 cascade, delete시 cascade
        private String title;
        private String content;
        private int price;
        private int stock;
        private Category category;
        private String createTime;
        private SaleStatus saleStatus = SaleStatus.READY;
        private int viewCount = 0;
        private int likeCount = 0;
        private int commentCount = 0;

        private List<String> fileNames;
        private boolean likeStatus = false;             //조회하는 자가 회원일 때, 게시글에 관심버튼 눌렀으면 누른상태로 보여주어야함
        private boolean menuStatus = false;             //조회하는 자가 회원이고, 게시글 작성자이면 수정 삭제버튼 보여줌

        public Response(Post post) {
            this.id = post.getId();
            this.writer = post.getWriter();
            this.title = post.getTitle();
            this.content = post.getContent();
            this.price = post.getPrice();
            this.stock = post.getStock();
            this.category = post.getCategory();
            this.createTime = post.getCreateTime();
            this.saleStatus = post.getSaleStatus();
            this.viewCount = post.getViewCount();
            this.likeCount = post.getLikeCount();
            this.commentCount = post.getCommentCount();
        }

        public Response(Long id, String writer, String title, String content, int price, int stock, Category category, String createTime, SaleStatus saleStatus, int viewCount, int likeCount, int commentCount, List<String> fileNames) {
            this.id = id;
            this.writer = writer;
            this.title = title;
            this.content = content;
            this.price = price;
            this.stock = stock;
            this.category = category;
            this.createTime = createTime;
            this.saleStatus = saleStatus;
            this.viewCount = viewCount;
            this.likeCount = likeCount;
            this.commentCount = commentCount;
            this.fileNames = fileNames;
        }

        public String getCreateTime() {
//        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//        방금전, n분전, 1시간 전.. 등 처리
//        if(){
//
//        }

            //2022-01-15 00:00:54.0 -> 22-01-15 00:00
            if (createTime.length() == 21) {
                return createTime.substring(2, createTime.length() - 5);
                //DB에 저장될때도 substring되어서 저장되어버림.. 뷰에서 가져올때만 처리되도록
            }
            return createTime;
        }

        //타임리프 편의메서드
        public String getCategory() {
            return category.getValue();
        }

        public String getSaleStatus() {
            return saleStatus.getValue();
        }

        public static List<PostDTO.Response> ListPostToListDto(List<Post> posts){
            return posts.stream()
                    .map(post -> new PostDTO.Response(post)).collect(Collectors.toList());
        }
    }

}
