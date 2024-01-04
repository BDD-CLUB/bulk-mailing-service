package io.springbatch.springbatch.member.service;

import io.springbatch.springbatch.global.jwt.JwtTokenFactory;
import io.springbatch.springbatch.global.redis.RedisUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.springbatch.springbatch.global.jwt.JwtType.*;

@Service
@RequiredArgsConstructor
public class AuthCookieService {

    private final JwtTokenFactory jwtTokenFactory;
    private final RedisUtil redisUtil;

    public void setNewCookie(String authId, String role, String userAgent, HttpServletResponse response) {
        String newRefreshToken = jwtTokenFactory.createAccessToken(REFRESH_TOKEN, authId, role);
        setTokenInCookie(response, newRefreshToken, (int) REFRESH_TOKEN.getExpiredMillis() / 1000, REFRESH_TOKEN.getTokenName());
        String newAccessToken = jwtTokenFactory.createAccessToken(ACCESS_TOKEN, authId, role);
        setTokenInCookie(response, newAccessToken, (int) ACCESS_TOKEN.getExpiredMillis() / 1000, ACCESS_TOKEN.getTokenName());

        redisUtil.setDataExpire(JwtTokenFactory.getRefreshTokenKeyForRedis(authId, userAgent), newRefreshToken, REFRESH_TOKEN.getExpiredMillis());
    }

    private void setTokenInCookie(HttpServletResponse response, String token, int expiredMillis, String cookieName) {
        ResponseCookie cookie = ResponseCookie.from(cookieName, token)
                .path("/")
                .sameSite("None")
                .httpOnly(true)
                .maxAge(expiredMillis)
                .secure(true)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

}
