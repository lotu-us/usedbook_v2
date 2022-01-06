package team.hello.usedbook.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter @ToString
@NoArgsConstructor @AllArgsConstructor
public class Comment {
    private Long id;
    private Long postId;
    private String writer;
    private String content;
    private Long parentId;  //부모댓글의 id값
    private int depth;      //댓글의 깊이. 0이면 댓글, 1이면 답글, 2이면 대댓글
    private LocalDateTime createTime;
}
