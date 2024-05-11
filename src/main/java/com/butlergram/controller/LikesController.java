package com.butlergram.controller;

import com.butlergram.dto.CMRespDto;
import com.butlergram.service.LikesService;
import com.butlergram.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;
    private final UserService userService;

    @PostMapping("/post/{postId}/likes")
    public ResponseEntity<?> likes(@PathVariable("postId") Long postId, Principal principal){
        likesService.likes(postId, userService.findByUsername(principal.getName()).getId());
        return new ResponseEntity<>(new CMRespDto<>(1, "좋아요 성공", null), HttpStatus.CREATED); // CREATED(201)가 OK(200)보다 더 정확한 상태 코드.
    }

    @DeleteMapping("/post/{postId}/likes")
    public ResponseEntity<?> unLikes(@PathVariable("postId") Long postId, Principal principal){
        likesService.unLikes(postId, userService.findByUsername(principal.getName()).getId());
        return new ResponseEntity<>(new CMRespDto<>(1, "좋아요취소 성공", null), HttpStatus.OK);
    }
}
