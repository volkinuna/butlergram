package com.butlergram.controller;

import com.butlergram.dto.CMRespDto;
import com.butlergram.dto.SubscribeDto;
import com.butlergram.dto.UserProfileDto;
import com.butlergram.dto.UserUpdateDto;
import com.butlergram.entity.Users;
import com.butlergram.service.AuthService;
import com.butlergram.service.SubscribeService;
import com.butlergram.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final SubscribeService subscribeService;

    @GetMapping("/user/{userId}")
    public String profile(@PathVariable(value = "userId") Long userId, Model model, Principal principal) {
        UserProfileDto userProfileDto = userService.profile(userId, userService.findByUsername(principal.getName()).getId());
        model.addAttribute("userProfileDto", userProfileDto);
        return "user/profile";
    }

    @PutMapping("/user/{principalId}/profileImageUrl")
    public ResponseEntity<?> profileImageUrlUpdate(@PathVariable("principalId") Long principalId, MultipartFile profileImageFile,
                                                   Principal principal){
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
        return "user/update";
    }


    @PostMapping("/user/{id}")
    public CMRespDto<?> update(@PathVariable("id") Long id, @Valid UserUpdateDto userUpdateDto,
            BindingResult bindingResult, Principal principal) {
        Users userEntity =  userService.update(id, userUpdateDto.createUser());
        //principalDetails.setUser(userEntity); // 세션 정보 변경
        return new CMRespDto<>(1, "회원수정완료", userEntity);
    }
}
