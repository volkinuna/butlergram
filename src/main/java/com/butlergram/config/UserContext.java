package com.butlergram.config;

import com.butlergram.entity.Users;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class UserContext extends User {

    //Authentication 객체에 저장하고 싶은 값을 필드로 지정
    private final Long id;
    private final String email;
    private final String name;

    public UserContext(Users users, List<GrantedAuthority> authorities) {
        super(users.getUsername(), users.getPassword(), authorities); //User 생성자 실행
        this.id = users.getId();
        this.email = users.getEmail();
        this.name = users.getName();
    }
}
