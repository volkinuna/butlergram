package com.butlergram.repository;

import com.butlergram.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    @Modifying // INSERT, DELETE, UPDATE 를 네이티브 쿼리로 작성하려면 해당 어노테이션 필요!!
    @Query(value = "INSERT INTO likes(post_id, user_id, reg_time) VALUES(:postId, :principalId, now())", nativeQuery = true)
    int likes(@Param("postId") Long postId, @Param("principalId") Long principalId);

    @Modifying
    @Query(value = "DELETE FROM likes WHERE post_id = :postId AND user_id = :principalId", nativeQuery = true)
    int unLikes(@Param("postId") Long postId, @Param("principalId") Long principalId);
}
