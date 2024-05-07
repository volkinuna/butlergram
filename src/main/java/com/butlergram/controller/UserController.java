package com.butlergram.controller;

import com.butlergram.dto.UserProfileDto;
import com.butlergram.service.AuthService;
import com.butlergram.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user/{userId}/profile")
    public String profile(@PathVariable(value = "userId") Long userId, Model model, Principal principal) {
        UserProfileDto userProfileDto = userService.profile(userId, userService.findByUserName(principal.getName()).getId());
        model.addAttribute("userProfileDto", userProfileDto);
        return "user/profile";
    }

    @GetMapping("/user/{id}/update")
    public String update(@PathVariable("id") Long id, Principal principal) {
        return "user/update";
    }
}
