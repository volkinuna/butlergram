package com.butlergram.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.awt.*;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
@ToString
public class User extends BaseEntity {

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
    private String name; //성명

    private String profileImgUrl; //프로필사진

    @Lob
    @Column(columnDefinition = "longtext")
    private String bio; //자기소개

    private String phone; //핸드폰

    private String gender; //성별

    private String role; //권한

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Post> posts;
}
