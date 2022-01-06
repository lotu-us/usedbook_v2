package team.hello.usedbook.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import team.hello.usedbook.domain.enums.Category;
import team.hello.usedbook.domain.enums.SaleStatus;

import java.time.LocalDateTime;

@Getter @ToString
@NoArgsConstructor @AllArgsConstructor
public class Post {
    private Long id;
    private String writer;
    private String title;
    private String content;
    private int price;
    private int stock;
    private Category category;
    private SaleStatus saleStatus;
    private LocalDateTime createTime;
    private int viewCount;
    private int likeCount;
}
