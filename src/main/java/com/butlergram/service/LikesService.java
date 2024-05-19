package com.butlergram.service;

import com.butlergram.repository.LikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;

    //좋아요
    @Transactional
    public void likes(Long postId, Long principalId) {
        likesRepository.likes(postId, principalId);
    }

    //좋아요 취소
    @Transactional
    public void unLikes(Long postId, Long principalId) {
        likesRepository.unLikes(postId, principalId);
    }
}
