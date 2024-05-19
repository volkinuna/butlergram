package com.butlergram.dto;

import com.butlergram.entity.Subscribe;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class SubscribeDto {

    private Long id; //로그인한 사용자의 id뿐만 아니라.. 누구를 구독할지(toUserId)..
    private String username; //유저네임때문에 필요
    private String profileImageUrl; //사진때문에 필요
    private Boolean subscribeState; //구독한 상태인지 아닌지..
    private Boolean equalUserState; //로그인한 사용자와 동일인인지 아닌지..

    public SubscribeDto(Long id, String username, String profileImageUrl, Boolean subscribeState, Boolean equalUserState) {
        this.id = id;
        this.username = username;
        this.profileImageUrl = profileImageUrl;
        this.subscribeState = subscribeState;
        this.equalUserState = equalUserState;
    }

    private static ModelMapper modelMapper = new ModelMapper();

    public static SubscribeDto of(Subscribe subscribe) {
        return modelMapper.map(subscribe, SubscribeDto.class);
    }
}