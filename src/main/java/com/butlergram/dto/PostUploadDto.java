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

    private MultipartFile file;

    private String caption;

    private static ModelMapper modelMapper = new ModelMapper();

    //dto -> entity
    public Post createPost(String postImageUrl, Users users) {
        Post post = modelMapper.map(this, Post.class);
        post.setPostImageUrl(postImageUrl);
        post.setUsers(users);
        return post;
    }
}
