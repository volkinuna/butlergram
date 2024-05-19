package com.butlergram.dto;

import com.butlergram.entity.Subscribe;
import com.butlergram.entity.Users;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;

@Getter
@Setter
public class UserUpdateDto {

    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String name; // 필수

    private String username; //유저네임

    @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
    @Length(min = 8, max = 16, message = "비밀번호는 8~16자 사이로 입력해 주세요.")
    private String password; // 필수

    private String email; //이메일

    private String profileImageUrl;
    private String bio;
    private String phone;
    private String gender;

    private static ModelMapper modelMapper = new ModelMapper();

    //dto -> entity
    public Users createUser() {
        return modelMapper.map(this, Users.class);
    }

    public static UserUpdateDto of(Users users) {
        return modelMapper.map(users, UserUpdateDto.class);
    }
}
