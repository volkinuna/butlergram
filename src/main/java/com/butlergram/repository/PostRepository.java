package com.butlergram.repository;

import com.butlergram.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.*;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "SELECT * FROM post WHERE userId IN (SELECT toUserId FROM subscribe WHERE fromUserId = :principalId) ORDER BY id DESC", nativeQuery = true)
    Page<Image> story(Long principalId, Pageable pageable); // fromUserId = :principalId <- 로그인한 id
    // 페이징이 Pageable pageable를 통해서 자동으로 됨 / 원래 List<>로 리턴 받았는데.. 페이징이 되니 Page<>로 리턴 받아야 한다.

    @Query(value = "SELECT i.* FROM post i INNER JOIN (SELECT postId, COUNT(postId) likeCount FROM likes GROUP BY postId) c ON i.id = c.postId ORDER BY likeCount DESC", nativeQuery = true)
    List<Post> popular();
}
