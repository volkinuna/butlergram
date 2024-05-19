package com.butlergram.service;

import com.butlergram.entity.Comment;
import com.butlergram.entity.Post;
import com.butlergram.entity.Users;
import com.butlergram.exception.CustomException;
import com.butlergram.repository.CommentRepository;
import com.butlergram.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    //댓글쓰기
    @Transactional
    public Comment commentWrite(String content, Long postId, Long userId) {

        Post post = new Post();
        post.setId(postId);

        Users userEntity = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setPost(post);
        comment.setUsers(userEntity);

        return commentRepository.save(comment);
    }

    //댓글삭제
    @Transactional
    public void commentDelete(Long id) {
        try {
            commentRepository.deleteById(id);
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }
}
