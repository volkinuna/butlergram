package com.butlergram.controller;

import com.butlergram.dto.CMRespDto;
import com.butlergram.dto.SubscribeDto;
import com.butlergram.dto.UserProfileDto;
import com.butlergram.entity.Users;
import com.butlergram.service.AuthService;
import com.butlergram.service.SubscribeService;
import com.butlergram.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final SubscribeService subscribeService;

    @GetMapping("/user/{userId}/profile")
    public String profile(@PathVariable(value = "userId") Long userId, Model model, Principal principal) {
        UserProfileDto userProfileDto = userService.profile(userId, userService.findByUsername(principal.getName()).getId());
        model.addAttribute("userProfileDto", userProfileDto);
        return "user/profile";
    }

    @GetMapping("/user/{id}/update")
    public String update(@PathVariable("id") Long id, Principal principal) {
        return "user/update";
    }

//    @PutMapping("/user/{principalId}/profileImageUrl")
//    public ResponseEntity<?> profileImageUrlUpdate(@PathVariable("principalId") Long principalId, MultipartFile profileImageFile,
//                                                   Principal principal){
//        Users userEntity = userService.profileImageUrlUpdate(principalId, profileImageFile);
//        principal.setUser(userEntity); // 세션 변경
//        return new ResponseEntity<>(new CMRespDto<>(1, "프로필사진 변경완료", null), HttpStatus.OK);
//    }

//    @GetMapping("/user/{userId}/subscribe")
//    public ResponseEntity<?> subscribeList(@PathVariable("userId") Long userId, Principal principal){
//
//        List<SubscribeDto> subscribeDto = subscribeService.구독리스트(principalDetails.getUser().getId(), pageUserId);
//
//        return new ResponseEntity<>(new CMRespDto<>(1, "구독자 정보 리스트를 불러오기 성공", subscribeDto), HttpStatus.OK);
//    }


//    @PutMapping("/user/{id}")
//    public CMRespDto<?> update(
//            @PathVariable int id,
//            @Valid UserUpdateDto userUpdateDto,
//            BindingResult bindingResult, // BindingResult bingingResult는 꼭! @Valid가 젹혀있는 다름 파라메터에 적어야됨!!
//            @AuthenticationPrincipal PrincipalDetails principalDetails) { // @AuthenticationPrincipal PrincipalDetails principalDetails <- 세션 정보에 접근하는 방법이었음.
//
//        User userEntity =  userService.회원수정(id, userUpdateDto.toEntity()); // User 오브젝트를 보낸다.
//        principalDetails.setUser(userEntity); // 세션 정보 변경
//        return new CMRespDto<>(1, "회원수정완료", userEntity); // 응답시에 userEntity의 모든 getter 함수가 호출되고 JSON으로 파싱하여 응답한다.
//    }
}
