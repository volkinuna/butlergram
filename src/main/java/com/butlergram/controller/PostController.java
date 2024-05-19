package com.butlergram.controller;

import com.butlergram.dto.CMRespDto;
import com.butlergram.dto.PostUploadDto;
import com.butlergram.entity.Post;
import com.butlergram.exception.CustomException;
import com.butlergram.service.PostService;
import com.butlergram.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;

    //스토리
    @GetMapping("/post")
    public ResponseEntity<?> story(Principal principal, @PageableDefault(size = 3) Pageable pageable){

        try {
            Page<Post> posts = postService.story(userService.findByUsername(principal.getName()).getId(), pageable);

            CMRespDto cmRespDto = new CMRespDto<>(1, "성공", posts);

            return new ResponseEntity<>(cmRespDto, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            return new ResponseEntity<>(new CMRespDto<>(2, "실패", null), HttpStatus.BAD_REQUEST);
        }
    }

    //스토리 삭제
    @DeleteMapping("/post/{postId}/delete")
    public @ResponseBody ResponseEntity deletePost(@PathVariable("postId") Long postId, Principal principal) {

        //1. 본인확인
        if (!postService.validatePost(postId, principal.getName())) {
            return new ResponseEntity<String>("스토리 삭제 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        //2. 스토리 삭제
        postService.deletePost(postId);

        return new ResponseEntity<Long>(postId, HttpStatus.OK);
    }

    @GetMapping("/post/upload")
    public String upload(Model model) {

        model.addAttribute("postUpdateDto", new PostUploadDto());

        return "post/upload";
    }

    @PostMapping("/post/story")
    public String postUpload(PostUploadDto postUploadDto, Principal principal) {

        if(postUploadDto.getFile().isEmpty()) {
            throw new CustomException("이미지가 첨부되지 않았습니다.");
        }

        postService.postUpload(postUploadDto, principal);

        return "redirect:/user/" + userService.findByUsername(principal.getName()).getId();
    }

    //인기 게시물(실제론 좋아요 게시물)
    @GetMapping("/post/popular")
    public String popular(Model model) {

        List<Post> posts = postService.popular();
        model.addAttribute("posts", posts);

        return "post/popular";
    }
}
