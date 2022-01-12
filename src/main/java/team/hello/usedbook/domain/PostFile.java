package team.hello.usedbook.domain;

import lombok.Getter;
import lombok.ToString;

@Getter @ToString
public class PostFile {
    private Long id;
    private Long postId;
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
