package com.butlergram.controller;

import com.butlergram.dto.CMRespDto;
import com.butlergram.dto.PostUploadDto;
import com.butlergram.entity.Post;
import com.butlergram.exception.CustomException;
import com.butlergram.service.PostService;
import com.butlergram.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.awt.*;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @GetMapping({"/","/post/story"})
    public String story() {
        return "post/story";
    }

    // API 구현하다면 이유 - 브라우저에서 요청하는게 아니라, 안드로이드나 IOS 요청시..(데이터를 줘야지 html파일을 줄 순 없으니깐..)
//    @GetMapping("/post/popular")
//    public String popularStory(Model model) {
//        // api는 데이터를 리턴하는 서버!! 인기페이지로 갈땐 ajax를 할게 아니라서 데이터를 리턴하는 서버를 만들 필요는 없음. 그래서 model에 담고 데이터를 들고 가기만 하면 됨.
//        List<Post> posts = postService.popular();
//        model.addAttribute("posts", posts);
//
//        return "post/popular";
//    }

    @GetMapping("/post/upload")
    public String upload() {
        return "post/upload";
    }

//    @PostMapping("/post") // 사용자로부터 데이터를 받고 서비스를 호출한다.
//    public String postUpload(PostUploadDto postUploadDto, Principal principal) {
//        // 서비스 호출                                  // @AuthenticationPrincipal PrincipalDetails principalDetails로 로그인한 유저정보 받기
//
//        //if(imageUploadDto.getFile().isEmpty()) {
//        //    throw new CustomValidationException("이미지가 첨부되지 않았습니다.", null);
//        //}
//
//        if(postUploadDto.getFile().isEmpty()) {
//            throw new CustomException("이미지가 첨부되지 않았습니다.");
//        }
//
//        //imageService.사진업로드(imageUploadDto, principalDetails);
//        //return "redirect:/user/"+principalDetails.getUser().getId();
//
//        postService.postUpload(postUploadDto, principal);
//        return "redirect:/user/" + userService.findByUsername(principal.getName()).getId();
//    }

//    @GetMapping("/post")
//    public ResponseEntity<?> story(Principal principal, @PageableDefault(size = 3) Pageable pageable){ // @PageableDefault을 붙이면 pageable를 이용해서 페이징을 할 수 있다.
//        Page<Post> posts = postService.story(userService.findByUsername(principal.getName()).getId(), pageable);
//        return new ResponseEntity<>(new CMRespDto<>(1, "성공", posts), HttpStatus.OK);
//    }
}
