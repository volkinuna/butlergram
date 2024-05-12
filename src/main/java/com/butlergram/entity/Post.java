package com.butlergram.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "post")
@Getter
@Setter
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String postImageUrl; //사진

    private String caption; //설명글

    @JoinColumn(name="user_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Users users;

    @OneToMany(mappedBy = "post")
    private List<Likes> likes;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    @Transient
    private boolean LikeState;

    @Transient
    private int likeCount;

    @Override
    public String toString() {
        return "{" +
                "\"id\": \"" + id + "\"," +
                "\"caption\": \"" + caption + "\"," +
                "\"postImageUrl\": \"" + postImageUrl + "\"" +
                "}";
    }
}
