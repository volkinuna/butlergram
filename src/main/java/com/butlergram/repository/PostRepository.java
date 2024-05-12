package com.butlergram.repository;

import com.butlergram.entity.Post;
import jakarta.websocket.server.PathParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.*;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "SELECT * FROM post ORDER BY reg_time DESC", nativeQuery = true)
    Page<Post> story(Pageable pageable); // fromUserId = :principalId <- 로그인한 id
    // 페이징이 Pageable pageable를 통해서 자동으로 됨 / 원래 List<>로 리턴 받았는데.. 페이징이 되니 Page<>로 리턴 받아야 한다.

    @Query(value = "SELECT i.* FROM post i INNER JOIN (SELECT post_id, COUNT(post_id) like_count FROM likes GROUP BY post_id) c ON i.id = c.post_id ORDER BY like_count DESC", nativeQuery = true)
    List<Post> popular();
}
