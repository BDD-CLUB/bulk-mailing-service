package io.springbatch.springbatch.member.entity.password;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordFactory implements PasswordEncoder{

    private static PasswordFactory instance = new PasswordFactory();

    private PasswordFactory() {
    }

    public static PasswordFactory getInstance() {
        return instance;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder().matches(rawPassword, encodedPassword);
    }
}
