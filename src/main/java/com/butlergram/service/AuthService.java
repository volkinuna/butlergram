package com.butlergram.service;

import com.butlergram.config.UserContext;
import com.butlergram.entity.Users;
import com.butlergram.repository.AuthRepository;
import com.butlergram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final AuthRepository authRepository;

    //회원가입
    public Users saveUser(Users user) {
        validateDuplicateUser(user);
        return authRepository.save(user); //회원정보 insert 후 해당 회원정보 다시 리턴
    }

    //회원 중복체크
    private void validateDuplicateUser(Users user) {
        Users findUser = authRepository.findByUsername(user.getUsername());

        if (findUser != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

//    @Override
//    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
//
//        Users user = authRepository.findByUsername(username);
//
//        if (user == null) {
//            throw new UsernameNotFoundException(username);
//        }
//
//        return User.builder()
//                .username(user.getUsername())
//                .password(user.getPassword())
//                .build();
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users users = authRepository.findByUsername(username);

        if (users == null) { //사용자가 없다면
            throw new UsernameNotFoundException(username);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();

        return new UserContext(users, authorities);
    }
}
