package com.butlergram.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.userdetails.User;

@Getter
@Setter
public class UserFormDto {

    @NotBlank(message = "유저네임은 필수 입력값입니다.")
    private String username; //유저네임

    @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
    @Length(min = 8, max = 16, message = "비밀번호는 8~16자 사이로 입력해 주세요.")
    private String password; //비밀번호

    @NotEmpty(message = "이메일은 필수 입력값입니다.")
    @Email(message = "이메일 형식으로 입력해 주세요.")
    private String email; //이메일

    @NotEmpty(message = "이름은 필수 입력값입니다.")
    private String name; //이름

}
