package io.springbatch.springbatch.member.entity;

import io.springbatch.springbatch.member.entity.password.Password;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "member_id", nullable = false)
    private String memberId;

    @Embedded
    private Password password;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "push_agree", nullable = false)
    private Boolean pushAgree;


    @Builder
    public Member(Long id, String name, String memberId, String password, String email, Boolean pushAgree) {
        this.id = id;
        this.name = name;
        this.memberId = memberId;
        this.password = Password.from(password);
        this.email = email;
        this.pushAgree = pushAgree;
    }
}
