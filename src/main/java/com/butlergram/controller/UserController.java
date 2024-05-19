package com.butlergram.controller;

import com.butlergram.dto.CMRespDto;
import com.butlergram.dto.SubscribeDto;
import com.butlergram.dto.UserProfileDto;
import com.butlergram.dto.UserUpdateDto;
import com.butlergram.entity.Users;
import com.butlergram.service.AuthService;
import com.butlergram.service.SubscribeService;
import com.butlergram.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final SubscribeService subscribeService;

    @GetMapping("/user/{userId}")
    public String profile(HttpServletRequest request, @PathVariable(value = "userId") Long userId, Model model, Principal principal) {
        Object httpStatus = request.getAttribute("HttpStatus");
        if (httpStatus != null && (int) httpStatus == HttpServletResponse.SC_UNAUTHORIZED)
            return "/auth/login";

        UserProfileDto userProfileDto = userService.profile(userId, userService.findByUsername(principal.getName()).getId());
        model.addAttribute("userProfileDto", userProfileDto);
        return "users/profile";
    }

    @PutMapping("/user/{principalId}/profileImageUrl")
    public ResponseEntity<?> profileImageUrlUpdate(@PathVariable("principalId") Long principalId, @RequestParam("profileImageFile") MultipartFile profileImageFile,
                                                   Principal principal){
        System.out.println("hey");
        Users userEntity = userService.profileImageUpdate(principalId, profileImageFile);
        //principal.setUser(userEntity); // 세션 변경
        return new ResponseEntity<>(new CMRespDto<>(1, "프로필사진 변경완료", null), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/subscribe")
    public ResponseEntity<?> subscribeList(@PathVariable("userId") Long userId, Principal principal){

        List<SubscribeDto> subscribeDto = subscribeService.subscribeList(userService.findByUsername(principal.getName()).getId(), userId);

        return new ResponseEntity<>(new CMRespDto<>(1, "구독자 정보 리스트를 불러오기 성공", subscribeDto), HttpStatus.OK);
    }

    @GetMapping("/user/{id}/update")
    public String update(@PathVariable("id") Long id, Principal principal, Model model) {
        Users users = userService.findByUsername(principal.getName());

        UserUpdateDto userUpdateDto = UserUpdateDto.of(users);

        userUpdateDto.getPassword();

        model.addAttribute("userUpdateDto", userUpdateDto);
        return "users/update";
    }


    @PostMapping("/user/{id}")
    public String update(@PathVariable("id") Long id, @Valid UserUpdateDto userUpdateDto,
            BindingResult bindingResult, Principal principal) {
        try {
            Users userEntity = userService.update(id, userUpdateDto.createUser());
            return "redirect:/user/" + userEntity.getId() ;
        } catch (Exception e) {
            e.printStackTrace();
            return "users/update";
        }
        //principalDetails.setUser(userEntity); // 세션 정보 변경

    }
}
