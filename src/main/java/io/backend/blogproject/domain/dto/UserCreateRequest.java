package io.backend.blogproject.domain.dto;

public record UserCreateRequest(
        String id,
        String password,
        String name
) {
}
