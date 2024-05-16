package com.butlergram.controller;

import com.butlergram.dto.UserFormDto;
import com.butlergram.entity.Users;
import com.butlergram.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    //로그인 화면
    @GetMapping(value = "/user/login") //localhost/user/login
    public String loginUser() {
        return "auth/signIn";
    }

    //회원가입 화면
    @GetMapping(value = "/user/new") //localhost/user/new
    public String userForm(Model model) {
        model.addAttribute("userFormDto", new UserFormDto());
        return "auth/signUp";
    }

    //실제 회원가입 처리
    @PostMapping(value = "/user/new")
    public String userForm(@Valid UserFormDto userFormDto,
                             BindingResult bindingResult, Model model) {

        //유효성 검증 에러 발생시 회원가입 페이지로 이동시킴
        if (bindingResult.hasErrors()) return "auth/signUp";

        //유효성 검사를 통과했다면 회원가입 진행
        try {
            //MemberFormDto -> Entity 객체로 변환
            Users user = Users.createUser(userFormDto, passwordEncoder);
;
            authService.saveUser(user);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "auth/signUp";
        }

        return "auth/signIn";
    }

    //로그인 실패시
    @GetMapping(value = "/user/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해 주세요.");
        return "auth/signIn"; //로그인 페이지로 그대로 이동
    }
}
