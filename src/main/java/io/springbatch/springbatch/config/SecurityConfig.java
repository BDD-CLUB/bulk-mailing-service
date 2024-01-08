package io.springbatch.springbatch.config;

import io.springbatch.springbatch.config.exception.FilterExceptionHandler;
import io.springbatch.springbatch.global.jwt.JwtTokenFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final FilterExceptionHandler filterExceptionHandler;
//    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtTokenFactory jwtTokenFactory;

//    @Bean
//    public AuthenticationManager authenticationManager() {
//        JwtAuthenticationProvider jwtAuthenticationProvider = new JwtAuthenticationProvider(jwtTokenFactory);
//
//        ProviderManager manager = new ProviderManager(jwtAuthenticationProvider);
//        manager.setEraseCredentialsAfterAuthentication(false);
//        return manager;
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "sign-in", "sign-up").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/sign-in")
                        .defaultSuccessUrl("/")
                        .failureHandler(((request, response, exception) -> response.sendRedirect("/sign-in")))
                        .permitAll()
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(filterExceptionHandler)
                )
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/**");
    }

}
