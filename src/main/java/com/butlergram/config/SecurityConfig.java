package com.butlergram.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        //1.페이지 접근에 관한 설정
        httpSecurity.authorizeHttpRequests(authorize -> authorize
                        //모든 사용자가 로그인(인증)없이 접근할 수 있도록 설정
                        .requestMatchers("/css/**", "/js/**", "/img/**", "/images/**", "/fonts/**").permitAll()
                        .requestMatchers("/", "/user/**", "/story/**").permitAll()
                        .requestMatchers("/favicon.ico", "/error").permitAll()
                        //관리자만 접근가능하도록 설정(인가)
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        //그 외의 페이지는 모두 로그인(인증)을 해야한다.
                        .anyRequest().authenticated()
                )
                //2. 로그인에 관한 설정
                .formLogin(formLogin -> formLogin
                        .loginPage("/user/login")
                        .defaultSuccessUrl("/")
                        .usernameParameter("userName")
                        .failureUrl("/user/login/error")
                )
                //3. 로그아웃에 관한 설정
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout"))
                        .logoutSuccessUrl("/")
                )
                //4. 인증되지 않은 사용자가 리소스에 접근시 설정
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                )
                .rememberMe(Customizer.withDefaults()); //로그인 이후 세션을 통해 로그인을 유지

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); //암호화 로직이 되어있음
    }
}