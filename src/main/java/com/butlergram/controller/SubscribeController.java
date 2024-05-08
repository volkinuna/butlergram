package com.butlergram.controller;

import com.butlergram.dto.CMRespDto;
import com.butlergram.service.SubscribeService;
import com.butlergram.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class SubscribeController {

    private final SubscribeService subscribeService;
    private final UserService userService;

    @PostMapping("/user/subscribe/{toUserId}")
    public ResponseEntity<?> subscribe(@PathVariable("toUserId") Long toUserId, Principal principal){
        subscribeService.subscribe(toUserId, userService.findByUsername(principal.getName()).getId());
        return new ResponseEntity<>(new CMRespDto<>(1, "구독하기 성공", null), HttpStatus.OK);
    }

    @DeleteMapping("/user/subscribe/{toUserId}")
    public ResponseEntity<?> unSubscribe(@PathVariable("toUserId") Long toUserId, Principal principal){
        subscribeService.unSubscribe(toUserId, userService.findByUsername(principal.getName()).getId());
        return new ResponseEntity<>(new CMRespDto<>(1, "구독취소하기 성공", null), HttpStatus.OK);
    }
}
