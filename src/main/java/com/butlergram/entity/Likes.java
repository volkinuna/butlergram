package com.butlergram.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(
            name = "likes_uk", //유니크 제약조건 이름
            columnNames = {"image_id", "user_id"} //제약조건 / 한명의 user는 하나의 post에 두번의 좋아요를 할 수 없다.
        )
    }
)
@Getter
@Setter
@ToString
public class Likes extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "image_id")
    @ManyToOne
    private Post post;

    //@JsonIgnoreProperties({"images"}) // Postman으로 확인시 -> user의 likes의 images가 무한참조 확인됨.
    @JoinColumn(name = "user_id")
    @ManyToOne
    private Users user;
}
