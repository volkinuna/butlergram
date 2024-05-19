package com.butlergram.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    @GetMapping({"/", "/post/story"})
    public String main(HttpServletRequest request) {
        // 로그인 되어 있지 않다면 로그인 페이지로
        Object httpStatus = request.getAttribute("HttpStatus");
        if (httpStatus != null && (int) httpStatus == HttpServletResponse.SC_UNAUTHORIZED)
            return "/auth/login";

        return "post/story";
    }
}
