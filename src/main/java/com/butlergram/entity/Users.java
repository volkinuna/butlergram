package com.butlergram.entity;

import com.butlergram.dto.UserFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
@ToString
public class Users extends BaseEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 100)
    private String userName; //유저네임

    @Column(nullable = false)
    private String password; //비밀번호

    @Column(nullable = false)
    private String email; //이메일

    @Column(nullable = false, length = 100)
    private String name; //이름

    private String profileImgUrl; //프로필사진

    @Lob
    @Column(columnDefinition = "longtext")
    private String bio; //자기소개

    private String phone; //핸드폰

    private String gender; //성별

    private String role; //권한

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Post> posts;

    //UserFormDto -> User 엔티티 객체로 변환
    public static Users createUser(UserFormDto userFormDto, PasswordEncoder passwordEncoder) {

        String password = passwordEncoder.encode(userFormDto.getPassword());

        Users user = new Users();

        user.setUserName(userFormDto.getUserName());
        user.setEmail(userFormDto.getEmail());
        user.setName(userFormDto.getName());
        user.setPassword(password);

        return user;
    }
}
