package com.butlergram.dto;

import com.butlergram.entity.Users;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileDto {

    private boolean pageOwnerState;
    private int imageCount;
    private boolean subscribeState;
    private int subscribeCount;
    private Users users;
}
