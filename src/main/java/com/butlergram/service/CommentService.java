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
    public Comment commentWrite(String content, Long postId, Long userId) { // 받아서 오브젝트를 insert

        // Tip!! (가짜 객체를 만든다. 객체를 만들때 id값만 insert 할 수 있다.)
        // 대신 return시에 image객체와 id값만 가지고 있는 빈 객체를 리턴받는다.
        Post post = new Post();
        post.setId(postId); // 이 객체엔 id값만 들어간다. 실제로 DB에 id값만 들어가면 된다.

        Users userEntity = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setPost(post); // DB에서 Select해서 그 객체를 받아와서 넣어야하는데... 가짜 객체를 만들어서 넣으면 좀 쉽다.
        comment.setUser(userEntity);     // 이렇게 안하면 DB에서 ImageRepository에서 findById()로 찾아서 넣어야한다.

        return commentRepository.save(comment);
    }

    //댓글삭제
    @Transactional
    public void commentDelete(Long id) {
        try {
            commentRepository.deleteById(id);
        } catch (Exception e) {
            throw new CustomException(e.getMessage()); // CustomException - html파일을 리턴하는 컨트롤러 / CustomApiException - 데이터를 리턴하는 컨트롤러
        }                                                                                       // CustomValidation쪽 Exception들은 제일 처음 어떤 값을 받을때 그 부분에 대해서만 처리하면 된다.
    }
}
