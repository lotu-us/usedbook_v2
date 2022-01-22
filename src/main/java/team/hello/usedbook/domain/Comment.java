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

}
