package team.hello.usedbook.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.hello.usedbook.domain.Comment;
import team.hello.usedbook.domain.enums.SaleStatus;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


public class CommentDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EditForm{
        @NotNull(message = "글 번호를 입력해주세요")
        private Long postId;

        private String writer;

        @NotBlank(message = "댓글 내용을 입력해주세요")
        private String content;

        private Long parentId = 0L; //부모댓글의 id값

        @Min(value = 0, message = "댓글의 깊이는 0부터 입니다.")
        @Max(value = 2, message = "댓글의 깊이는 2까지 입니다.")
        private int depth = 0;  //댓글의 깊이. 0이면 댓글, 1이면 답글, 2이면 대댓글

        private String createtTime;
    }

    @Data
    @NoArgsConstructor
    public static class Response{
        private Long id;
        private Long postId;
        private String writer;
        private String content;
        private Long parentId;  //부모댓글의 id값
        private int depth;      //댓글의 깊이. 0이면 댓글, 1이면 답글, 2이면 대댓글
        private String createTime;
        private int viewStatus; //1이면 게시된 것, 0이면 삭제처리된 것

        private boolean commentMenu = false;            //조회하는 자가 댓글 작성자이면 댓글 수정 삭제 가능

        public Response(Comment comment) {
            this.id = comment.getId();
            this.postId = comment.getId();
            this.writer = comment.getWriter();
            this.content = comment.getContent();
            this.parentId = comment.getParentId();
            this.depth = comment.getDepth();
            this.createTime = comment.getCreateTime();
            this.viewStatus = comment.getViewStatus();
        }

        public void deletedCommentHidePrivacy(){
            this.writer="";
            this.content="";
            this.createTime="";
        }

        //타임리프 편의메서드
        public String getCreateTime() {
            //2022-01-15 00:00:54.0 -> 22-01-15 00:00
            if (createTime.length() == 21) {
                return createTime.substring(2, createTime.length() - 5);
                //DB에 저장될때도 substring되어서 저장되어버림.. 뷰에서 가져올때만 처리되도록
            }
            return createTime;
        }

    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DashboardResponse{
        private Long postId;
        private String postSaleStatus;
        private String postTitle;
        private String commentContent;
        private String commentCreatetime;

        //타임리프 편의메서드
        public String getCommentCreatetime() {
            //2022-01-15 00:00:54.0 -> 22-01-15 00:00
            if (commentCreatetime.length() == 21) {
                return commentCreatetime.substring(2, commentCreatetime.length() - 5);
                //DB에 저장될때도 substring되어서 저장되어버림.. 뷰에서 가져올때만 처리되도록
            }
            return commentCreatetime;
        }

        public String getPostSaleStatus() {
            SaleStatus saleStatus = SaleStatus.valueOf(postSaleStatus.toUpperCase());
            return saleStatus.getValue();
        }

    }

}
