package com.butlergram.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {

    // @NotBlank = 빈값이거나 null, 빈 공백(스페이스)까지 체크 / @NotEmpty = 빈값이거나 null 체크 / @NotNull = null값 체크
    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String content;

    @NotNull
    private Long postId;
}
