package com.butlergram.service;

import com.butlergram.dto.PostUploadDto;
import com.butlergram.entity.Post;
import com.butlergram.entity.Users;
import com.butlergram.repository.PostRepository;
import com.butlergram.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    //스토리
    @Transactional(readOnly = true)
    public Page<Post> story(Long principalId, Pageable pageable) throws Exception {

        Page<Post> posts = postRepository.story(pageable);

        //게시물에 좋아요 상태 담기
        posts.forEach((post)->{

            post.setLikeCount(post.getLikes().size());

            post.getLikes().forEach((like)->{
                if(like.getUser().getId() == principalId) {
                    post.setLikeState(true);
                }
            });
        });

        return posts;
    }

    @Value("${imgLocation}")
    private String imgLocation;

    //사진 업로드
    @Transactional
    public void postUpload(PostUploadDto postUploadDto, Principal principal) {

        UUID uuid = UUID.randomUUID();
        String imageFileName = uuid + "_" + postUploadDto.getFile().getOriginalFilename();

        Path imageFilePath = Paths.get(imgLocation+imageFileName);

        try {
            Files.write(imageFilePath, postUploadDto.getFile().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Post post = postUploadDto.createPost(imageFileName, userRepository.findByUsername(principal.getName()));
        postRepository.save(post);
    }

    //본인 확인(현재 로그인한 사용자와 주문 데이터를 생성한 사용자가 같은지 검사)
    @Transactional(readOnly = true)
    public boolean validatePost(Long postId, String username) {
        //로그인한 사용자 찾기
        Users curUser = userRepository.findByUsername(username);

        //스토리 내역
        Post post = postRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);

        Users savedUser = post.getUsers(); //등록한 사용자 찾기

        //로그인한 사용자의 유저네임과 주문한 사용자의 유저네임이 같은지 비교
        if (!StringUtils.equals(curUser.getUsername(), savedUser.getUsername())) {
            return false;
        }

        return true;
    }

    //스토리 삭제
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);

        postRepository.delete(post);
    }

    //인기사진 리스트
    @Transactional(readOnly = true)
    public List<Post> popular() {
        return postRepository.popular();
    }
}
