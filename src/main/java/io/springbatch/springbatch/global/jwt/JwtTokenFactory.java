package io.springbatch.springbatch.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenFactory {

    private static final String ROLES = "roles";
    private final Key secretKey;

    public static String getRefreshTokenKeyForRedis(String authId, String userAgent) {
        String encodedUserAgent = Base64.getEncoder().encodeToString((userAgent == null ? "" : userAgent).getBytes());
        return "refreshToken:" + authId + ":" + encodedUserAgent;
    }

    public JwtTokenFactory(@Value("${spring.jwt.secret}") String secretKey) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String createAccessToken(JwtType jwtType, String authId, String role) {
        Claims claims = Jwts.claims().setSubject(authId);
        claims.put(ROLES, role);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtType.getExpiredMillis()))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
}
