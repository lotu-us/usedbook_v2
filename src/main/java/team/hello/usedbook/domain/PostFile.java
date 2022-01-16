package team.hello.usedbook.domain;

import lombok.Getter;
import lombok.ToString;

@Getter @ToString
public class PostFile {
    private Long id;
    private Long postId;    //post테이블의 id참조 중.   update시 no action, delete시 cascade
    private String filePath;
    private String fileName;

    private PostFile(){

    }

    public PostFile(Long postId, String filePath, String fileName) {
        this.postId = postId;
        this.filePath = filePath;
        this.fileName = fileName;
    }
}
