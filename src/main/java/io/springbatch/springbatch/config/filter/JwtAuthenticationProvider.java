package io.springbatch.springbatch.config.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.springbatch.springbatch.config.exception.EmptyJwtException;
import io.springbatch.springbatch.global.jwt.JwtTokenFactory;
import io.springbatch.springbatch.global.jwt.JwtType;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private static final String ROLES = "roles";
    private final JwtTokenFactory jwtTokenFactory;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String jwtAccessToken = authentication.getPrincipal().toString();

        try {
            Claims claims = getClaim(jwtAccessToken);
            JwtUserDetails jwtUserDetails = new JwtUserDetails(claims.getSubject(), claims.get(ROLES).toString());
            return new UsernamePasswordAuthenticationToken(jwtUserDetails, "", jwtUserDetails.getAuthorities());
        } catch (EmptyJwtException e) {
            throw new AuthenticationServiceException("Failed to authenticate JWT token", e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationFilter.class.isAssignableFrom(authentication);
    }

    protected static String getJwtAccessToken(HttpServletRequest request) {
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
