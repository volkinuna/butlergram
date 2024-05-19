package com.butlergram.controller;

import com.butlergram.dto.CMRespDto;
import com.butlergram.dto.CommentDto;
import com.butlergram.entity.Comment;
import com.butlergram.service.CommentService;
import com.butlergram.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;

    @PostMapping("/comment")
    public ResponseEntity<?> commentSave(@Valid @RequestBody CommentDto commentDto, Principal principal){

        Comment comment = commentService.commentWrite(commentDto.getContent(), commentDto.getPostId(),
                userService.findByUsername(principal.getName()).getId());

        return new ResponseEntity<>(new CMRespDto<>(1, "댓글쓰기 성공", comment), HttpStatus.CREATED);
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity<?> commentDelete(@PathVariable("id") Long id){

        commentService.commentDelete(id);

        return new ResponseEntity<>(new CMRespDto<>(1, "댓글삭제 성공", null), HttpStatus.OK);
    }
}
