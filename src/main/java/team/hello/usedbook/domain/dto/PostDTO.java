package team.hello.usedbook.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PostDTO {

    @Data
    @AllArgsConstructor
    public static class EditForm {
        @NotBlank(message = "제목을 입력해주세요")
        private String title;

        @NotBlank(message = "카테고리를 선택해주세요")
        private String category;

        @NotNull(message = "값을 입력해주세요")
        @Min(value = 1000, message = "가격을 1000원 이상으로 설정해주세요")
        private Integer price;

        @NotNull(message = "값을 입력해주세요")
        @Max(value = 100, message = "수량은 100개까지 설정할 수 있습니다")
        private Integer stock;

        @NotBlank(message = "내용을 입력해주세요")
        private String content;
    }
}
