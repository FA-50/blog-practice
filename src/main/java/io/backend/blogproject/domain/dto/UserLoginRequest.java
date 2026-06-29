package io.backend.blogproject.domain.dto;
public record UserLoginRequest(
        String userId,
        String password
) {
}
