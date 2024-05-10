package com.butlergram.repository;

import com.butlergram.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    @Modifying // INSERT, DELETE, UPDATE 를 네이티브 쿼리로 작성하려면 해당 어노테이션 필요!!
    @Query(value = "INSERT INTO likes(postId, userId, createDate) VALUES(:postId, :principalId, now())", nativeQuery = true)
    int likes(Long postId, Long principalId);

    @Modifying
    @Query(value = "DELETE FROM likes WHERE postId = :postId AND userId = :principalId", nativeQuery = true)
    int unLikes(Long postId, Long principalId);
}
