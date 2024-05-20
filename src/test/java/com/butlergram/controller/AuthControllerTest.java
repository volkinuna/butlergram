package com.butlergram.controller;

import com.butlergram.dto.UserFormDto;
import com.butlergram.entity.Users;
import com.butlergram.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc // mockMvc 테스트를 위해 어노테이션 선언
@TestPropertySource(locations = "classpath:application-test.properties")
public class AuthControllerTest {
    @Autowired
    private AuthService authService;

    @Autowired
    private MockMvc mockMvc; // 모형 객체 => 웹 브라우저에서 request 하는 것처럼 테스트 할 수 있는 객체

    @Autowired
    PasswordEncoder passwordEncoder;

    public Users createUser(String username, String password){
        UserFormDto userFormDto = new UserFormDto();
        userFormDto.setUsername(username);
        userFormDto.setEmail("coco@gmail.com");
        userFormDto.setName("최코코");
        userFormDto.setPassword(password);
        Users users = Users.createUser(userFormDto, passwordEncoder);

        return authService.saveUser(users);
    }

    @Test
    @DisplayName("사용자 계정 생성 테스트")
    public void signUpTest() throws Exception {
        String username = "coco";
        String password = "1234";
        Users savedUser = createUser(username, password);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo(username);
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    public void loginSuccessTest() throws Exception {
        String username = "coco";
        String password = "1234";
        createUser(username, password);

        mockMvc.perform(SecurityMockMvcRequestBuilders
                .formLogin().userParameter("username")
                .loginProcessingUrl("/auth/login") // 로그인 처리할 경로
                .user(username).password(password)
        ).andExpect(SecurityMockMvcResultMatchers.authenticated()); // 로그인이 성공하면 테스트 코드를 통과시킴
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    public void loginFailTest() throws Exception {
        String username = "coco";
        String password = "1234";
        createUser(username, password);

        mockMvc.perform(SecurityMockMvcRequestBuilders
                .formLogin().userParameter("username")
                .loginProcessingUrl("/auth/login") // 로그인 처리할 경로
                .user(username).password("12345") //비밀번호 일부러 다르게 입력
        ).andExpect(SecurityMockMvcResultMatchers.unauthenticated()); // 로그인이 실패하면 테스트 코드를 통과시킴
    }
}