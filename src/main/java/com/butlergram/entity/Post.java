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
@ToString
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imgUrl; //사진

    private String caption; //설명글

    @JoinColumn(name="user_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Users user;

    @OneToMany(mappedBy = "post")
    private List<Likes> likes;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;
}
