package com.butlergram.dto;

import com.butlergram.entity.Post;
import com.butlergram.entity.Users;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;

@Getter
@Setter
public class PostUploadDto {

    private MultipartFile file; // MultipartFile 타입에는 @NotBlank가 지원이 안된다.

    private String caption;

//    public Post toEntity(String postImageUrl, Users users) {
//        return Post.builder()
//                .caption(caption)
//                .postImageUrl(postImageUrl) // 정확한 경로가 들어가야함.
//                .user(users) // Image 객체는 user 정보가 필요함.
//                .build();
//    }

    private static ModelMapper modelMapper = new ModelMapper();

    //dto -> entity
    public Post createPost(String postImageUrl, Users users) {
        Post post = modelMapper.map(this, Post.class);
        post.setPostImageUrl(postImageUrl);
        post.setUsers(users);
        return post;
    }
}
