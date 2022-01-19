package team.hello.usedbook.domain;

import lombok.Getter;
import lombok.ToString;

@Getter @ToString
public class Comment {
    private Long id;
    private Long postId;
    private String writer;
    private String content;
    private Long parentId;  //부모댓글의 id값
    private int depth;      //댓글의 깊이. 0이면 댓글, 1이면 답글, 2이면 대댓글
    private String createTime;
    private int viewStatus; //1이면 게시된 것, 0이면 삭제처리된 것

    private Comment() {
    }

    public Comment(Long postId, String content) {
        this.postId = postId;
        this.content = content;
    }

    public Comment(Long postId, String writer, String content, Long parentId, int depth, String createTime) {
        this.postId = postId;
        this.writer = writer;
        this.content = content;
        this.parentId = parentId;
        this.depth = depth;
        this.createTime = createTime;
    }


    public String getCreateTime() {
//        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//        방금전, n분전, 1시간 전.. 등 처리
//        if(){
//
//        }

        //2022-01-15 00:00:54.0 -> 22-01-15 00:00
        if(createTime.length() == 21){
            return createTime.substring(2, createTime.length()-5);
            //DB에 저장될때도 substring되어서 저장되어버림.. 뷰에서 가져올때만 처리되도록
        }
        return createTime;
    }

}
