package com.butlergram.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(
            name = "subscribe_uk", //유니크 제약조건 이름
            columnNames = {"from_user_id", "to_user_id"} //제약조건
        )
    }
)
@Getter
@Setter
@ToString
public class Subscribe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "from_user_id")
    @ManyToOne
    private Users fromUser;

    @JoinColumn(name = "to_user_id")
    @ManyToOne
    private Users toUser;
}
