package team.hello.usedbook.domain.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import team.hello.usedbook.domain.enums.Category;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public class PostDTO {

    @Data
    public static class EditForm {
        @NotBlank
        private String title;

        @NotNull(message = "카테고리를 선택해주세요")
        private Category category;

        @NotNull(message = "값을 입력해주세요")
        @Min(value = 1000, message = "가격을 1000원 이상으로 설정해주세요")
        private Integer price;

        @NotNull(message = "값을 입력해주세요")
        private Integer stock;

        @NotBlank
        private String content;

        @NotNull(message = "이미지는 최소 1개 이상 있어야합니다.")
        private List<MultipartFile> fileList;
    }
}
