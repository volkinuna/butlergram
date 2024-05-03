package com.butlergram.repository;

import com.butlergram.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<Users, Long> {

    Users findByUserName(String userName);
}
