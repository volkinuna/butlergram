package com.butlergram.repository;

import com.butlergram.entity.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {

    @Modifying
    @Query(value = "INSERT INTO subscribe(from_user_id, to_user_id, createDate) VALUES(:from_user_id, :to_user_id, now())", nativeQuery = true)
    void subscribe(@Param("from_user_id") Long fromUserId, @Param("to_user_id") Long toUserId); //구독

    @Modifying
    @Query(value = "DELETE FROM subscribe WHERE from_user_id = :from_user_id AND to_user_id = :to_user_id", nativeQuery = true)
    void unSubscribe(@Param("from_user_id") Long fromUserId, @Param("to_user_id") Long toUserId); //구독취소

    @Query(value = "SELECT COUNT(*) FROM subscribe WHERE from_user_id = :principal_id AND to_user_id = :user_id", nativeQuery = true)
    int subscribeState(@Param("principal_id") Long principalId, @Param("user_id") Long userId);

    @Query(value = "SELECT COUNT(*) FROM subscribe WHERE from_user_id = :user_id", nativeQuery = true)
    int subscribeCount(@Param("user_id") Long userId);
}
