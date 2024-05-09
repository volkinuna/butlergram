package com.butlergram.dto;

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

    private String bio;
    private String phone;
    private String gender;

//    public User toEntity() {
//        return User.builder()
//                .name(name) // Validation 체크.
//                .password(password) // 패스워드 미기재시 데이터베이스에 공백의 패스워드가 들어가므로 문제가 됨. Validation 체크.
//                .bio(bio)
//                .phone(phone)
//                .gender(gender)
//                .build();
//    }

    private static ModelMapper modelMapper = new ModelMapper();

    public static UserUpdateDto of(Users users) {
        return modelMapper.map(users, UserUpdateDto.class);
    }
}
