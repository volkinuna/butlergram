package com.butlergram.service;

import com.butlergram.dto.UserProfileDto;
import com.butlergram.dto.UserUpdateDto;
import com.butlergram.entity.Users;
import com.butlergram.repository.SubscribeRepository;
import com.butlergram.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final SubscribeRepository subscribeRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${imgLocation}")
    private String imgLocation;

    //회원 프로필사진 변경
    @Transactional
    public Users profileImageUpdate(Long principalId, MultipartFile profileImageFile) {
        UUID uuid = UUID.randomUUID();
        String imageFileName = uuid + "_" + profileImageFile.getOriginalFilename();

        Path imageFilePath = Paths.get(imgLocation+imageFileName);

        try {
            Files.write(imageFilePath, profileImageFile.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Users userEntity = userRepository.findById(principalId).orElseThrow(EntityNotFoundException::new);

        userEntity.setProfileImageUrl(imageFileName);

        return userEntity;
    }

    //유저 아이디(username)를 통해 유저 객체 받아오기
    public Users findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    //회원 프로필
    @Transactional(readOnly = true)
    public UserProfileDto profile(Long userId, Long principalId) {
        UserProfileDto userProfileDto = new UserProfileDto();

        Users userEntity = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);

        userProfileDto.setUsers(userEntity);
        userProfileDto.setPageOwnerState(userId == principalId);
        userProfileDto.setImageCount(userEntity.getPosts().size());

        int subscribeState = subscribeRepository.subscribeState(principalId, userId);
        int subscribeCount = subscribeRepository.subscribeCount(userId);

        userProfileDto.setSubscribeState(subscribeState == 1);
        userProfileDto.setSubscribeCount(subscribeCount);

        // 프로필페이지 게시물에 좋아요 카운트 추가하기
        userEntity.getPosts().forEach((post)->{
            post.setLikeCount(post.getLikes().size());
        });

        return userProfileDto;
    }

    //회원 수정
    @Transactional
    public Users update(Long id, Users users) throws Exception {

        Users userEntity = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        userEntity.setName(users.getName());

        String rawPassword = users.getPassword();
        String encPassword = passwordEncoder.encode(rawPassword);
        userEntity.setPassword(encPassword);

        userEntity.setBio(users.getBio());
        userEntity.setPhone(users.getPhone());
        userEntity.setGender(users.getGender());

        return userEntity;
    }
}
