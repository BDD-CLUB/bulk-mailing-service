package io.springbatch.springbatch.config.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.springbatch.springbatch.config.exception.EmptyJwtException;
import io.springbatch.springbatch.global.jwt.JwtTokenFactory;
import io.springbatch.springbatch.global.jwt.JwtType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final String ROLES = "roles";
    private final JwtTokenFactory jwtTokenFactory;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        final String jwtAccessToken = getJwtAccessToken(request);
        final long authId = Long.parseLong(getClaim(jwtAccessToken).getSubject());
        final String rule = getClaim(jwtAccessToken)
                .get(ROLES)
                .toString();

        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(authId, rule);
        setDetails(request, authRequest);
        return getAuthenticationManager().authenticate(authRequest);

    }

    private static String getJwtAccessToken(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(JwtType.ACCESS_TOKEN.getTokenName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(EmptyJwtException::new);
    }

    private Claims getClaim(String jwtAccessToken) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtTokenFactory.getSecretKey())
                .build()
                .parseClaimsJws(jwtAccessToken)
                .getBody();
    }
}
