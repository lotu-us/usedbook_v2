package team.hello.usedbook.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter @ToString
@NoArgsConstructor @AllArgsConstructor
public class PostFile {
    private Long id;
    private Long postId;
    private String filePath;
    private String fileName;
}
