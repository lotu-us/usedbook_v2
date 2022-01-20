package team.hello.usedbook.domain;

import lombok.Getter;
import lombok.ToString;
import team.hello.usedbook.domain.enums.Category;
import team.hello.usedbook.domain.enums.SaleStatus;

@Getter @ToString
public class Post {
    private Long id;

    private String writer;  //member테이블의 email참조 중. update시 cascade, delete시 cascade
    private String title;
    private String content;
    private int price;
    private int stock;
    private Category category;
    private String createTime;

    private SaleStatus saleStatus = SaleStatus.READY;
    private int viewCount = 0;
    private int likeCount = 0;
    private int commentCount = 0;

    private Post(){

    }

    public Post(String title, String content, int price, int stock, SaleStatus saleStatus ,Category category) {
        this.title = title;
        this.content = content;
        this.price = price;
        this.stock = stock;
        this.saleStatus = saleStatus;
        this.category = category;
    }

    public Post(String writer, String title, String content, int price, int stock, Category category, String createTime) {
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.createTime = createTime;
    }

}
