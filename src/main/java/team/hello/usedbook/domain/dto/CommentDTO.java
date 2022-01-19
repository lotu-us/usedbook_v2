package team.hello.usedbook.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

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
