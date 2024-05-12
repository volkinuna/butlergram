package com.butlergram.service;

import com.butlergram.dto.PostUploadDto;
import com.butlergram.entity.Post;
import com.butlergram.repository.PostRepository;
import com.butlergram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    //인기사진 리스트
    @Transactional(readOnly = true)
    public List<Post> popular() {
        return postRepository.popular();
    }

    //스토리
    @Transactional(readOnly = true) // Insert 할꺼 아니니깐.. 읽기만 할꺼니깐... // 영속성 컨텍스트 변경 감지를 해서, 더티체킹, flush(반영) <- readOnly = true를 하면 이걸 안함.
    public Page<Post> story(Long principalId, Pageable pageable) throws Exception {
        Page<Post> posts = postRepository.story(pageable);

        // posts에 좋아요 상태 담기
        posts.forEach((post)->{ // 로그인한 user가 구독하고 있는 이미지들을 for문을 돌려서 하나씩 뽑아낸다.

            post.setLikeCount(post.getLikes().size()); // 좋아요 갯수 표시

            post.getLikes().forEach((like)->{ // 뽑아낸 이미지의 좋아요 정보를 다 가져와서 그 좋아요가 로그인한 user가 좋아요한건지 찾아야한다.
                if(like.getUser().getId() == principalId) { // 두개가 같으면 좋아요를 했다. 같지 않으면 좋아요 안함.
                    post.setLikeState(true);
                }
            });

        });

        return posts; // 원래 List<>로 리턴 받았는데.. 페이징이 되니 Page<>로 리턴 받아야 한다.
    }


    @Value("${imgLocation}") // application.yml에 업로드된 사진 저장할 공간을 정의한 경로
    private String imgLocation; // private String uploadFolder = "C:/workspace/springbootwork/photogram_upload/"; <- 로 해도 되지만..

    //사진 업로드
    // 2가지 이상의 로직이 하나의 서비스가 되는걸 트랜젹션(일의 최소 단위)이라고 한다.
    @Transactional // <- 서비스단에서 DB에 변형을 줄때는 꼭 걸어줘야함. 이유는?  여러가지 insert나 update가 있을 경우.. 다 성공을 해야 트랜잭션이 정상적으로 발동을 해서 Rollback을 안하고 commit을 한다.
    public void postUpload(PostUploadDto postUploadDto, Principal principal) {
        UUID uuid = UUID.randomUUID();
        // UUID(Universally Unique IDentifier 범용 고유 식별자)란? 네트워크 상에서 고유성이 보장되는 id를 만들기 위한 표준 규약. 유일성이 보장. 128비트 숫자, 8-4-4-4-12로 5개의 그룹으로 구분(ex. 550e8400-e29b-a716-446655440000)
        String imageFileName = uuid + "_" + postUploadDto.getFile().getOriginalFilename(); // 실제 파일명이 imageFileName로 들어간다.

        Path imageFilePath = Paths.get(imgLocation+imageFileName);

        // 통신이 일어나거나, I/O가 일어날때 -> 예외가 발생할 수 있다.
        try {
            Files.write(imageFilePath, postUploadDto.getFile().getBytes()); // 첫번째 Path, 두번째 실제 이미지 파일을 바이트화해서, 세번째 생략
        } catch (Exception e) {
            e.printStackTrace();
        }

        // DB의 image 테이블에 저장
        Post post = postUploadDto.createPost(imageFileName, userRepository.findByUsername(principal.getName())); // imageUploadDto의 내용을 가지고 Image 객체로 변환하는게 필요하다. / imageFileName = 5cf6237d-c404-836b-e55413ed0e49-cat.jpg
        postRepository.save(post); // 어차피 void라 아래코드에서 이걸로...
        // Image imageEntity = imageRepository.save(image); <- imageUploadDto를 넣을 수 없고, Image 객체를 넣어야한다.
        // imageUploadDto를 image에 집어넣는 로직이 필요한데.. 그건 데이터트랜스퍼오브젝트(web-dto-image-ImageUploadDto)에서 만들면 된다.

        // System.out.println(imageEntity); <- 주석을 풀면 사진등록 오류가 나는 이유는? Image의 Getter가 다 출력이 됨. Image 클래스와 User 클래스를 반복으로 돌아감. User 클래스 출력에서 문제가 발생.
        // imageEntity를 출력할때 sysout 내부적으론 imageEntity.toSting()이 자동으로 호출됨.
    }
}
