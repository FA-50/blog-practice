package io.backend.blogproject.domain.dto;

public record KeyPair(
        String accessToken,
        String refreshToken
) {
}
