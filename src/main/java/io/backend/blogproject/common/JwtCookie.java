package io.backend.blogproject.common;

import org.springframework.http.ResponseCookie;

import java.time.Duration;

public final class JwtCookie {
    public static final String ACCESS_TOKEN = "access_token";

    private JwtCookie() {
    }

    public static ResponseCookie accessToken(String token, long validTimeMillis, boolean secure) {
        return ResponseCookie.from(ACCESS_TOKEN, token)
                .httpOnly(true)
                .secure(secure)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofMillis(validTimeMillis))
                .build();
    }

    public static ResponseCookie expired(boolean secure) {
        return ResponseCookie.from(ACCESS_TOKEN, "")
                .httpOnly(true)
                .secure(secure)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ZERO)
                .build();
    }
}
