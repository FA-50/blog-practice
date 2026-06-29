package io.backend.blogproject.domain.dto;

public record UserLoginResponse(
        String accessToken,
        String refreshToken,
        String tokenType
) {
}
