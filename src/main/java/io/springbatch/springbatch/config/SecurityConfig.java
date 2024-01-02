package io.springbatch.springbatch.config;

import io.springbatch.springbatch.config.exception.FilterExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final FilterExceptionHandler filterExceptionHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/sign-up").permitAll()
                        .requestMatchers("/**").hasRole("USER")
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/sign-in")
                        .defaultSuccessUrl("/")
                        .failureHandler(((request, response, exception) -> response.sendRedirect("/sign-in")))
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(filterExceptionHandler)
                )
                .build();
    }

}
