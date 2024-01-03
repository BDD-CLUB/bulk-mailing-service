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

    @Column(name = "member_role", nullable = false)
    private MemberRoleType role;

    @Column(name = "push_agree", nullable = false)
    private Boolean pushAgree;

    @Builder
    public Member(Long id, String name, String memberId, Password password, String email, MemberRoleType role, Boolean pushAgree) {
        this.id = id;
        this.name = name;
        this.memberId = memberId;
        this.password = password;
        this.email = email;
        this.role = role;
        this.pushAgree = pushAgree;
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
