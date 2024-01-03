package io.springbatch.springbatch.member.entity.password;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = {"password"})
public class Password {

    private String password;

    public static Password from(String rawPassword) {
        return new Password(PasswordFactory.getInstance().encode(rawPassword));
    }
}
