package io.springbatch.springbatch.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "push_agree", nullable = false)
    private Boolean pushAgree;


    @Builder
    public Member(Long id, String name, String memberId, String password, String email, Boolean pushAgree) {
        this.id = id;
        this.name = name;
        this.memberId = memberId;
        this.password = password;
        this.email = email;
        this.pushAgree = pushAgree;
    }
}
