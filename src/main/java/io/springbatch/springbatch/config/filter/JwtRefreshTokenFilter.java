package io.springbatch.springbatch.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

//import static io.springbatch.springbatch.config.filter.JwtAuthenticationProvider.getJwtAccessToken;
//
//@Component
//@RequiredArgsConstructor
//public class JwtRefreshTokenFilter extends GenericFilterBean {
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//        String jwtAccessToken = getJwtAccessToken(httpRequest);
//
//    }
//}
