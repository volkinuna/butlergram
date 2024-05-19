package com.butlergram.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {

    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String content;

    @NotNull
    private Long postId;
}
