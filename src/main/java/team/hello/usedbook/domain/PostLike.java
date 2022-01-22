package team.hello.usedbook.domain;

import lombok.Getter;
import lombok.ToString;

@Getter @ToString
public class PostLike {
    private Long id;
    private Long memberId;
    private Long postId;

    public PostLike(Long memberId, Long postId) {
        this.memberId = memberId;
        this.postId = postId;
    }
}
