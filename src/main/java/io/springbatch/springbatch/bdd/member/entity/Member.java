package io.springbatch.springbatch.bdd.member.entity;

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

    @Column(name = "email", nullable = false)
    private String email;

    @Builder
    public Member(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Member(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    @Getter
    public enum MemberRoleType {
        ROLE_ADMIN(1),
        ROLE_USER(2);

        private final long id;

        MemberRoleType(long id) {
            this.id = id;
        }
    }

}
