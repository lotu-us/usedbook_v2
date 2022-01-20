package team.hello.usedbook.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
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
