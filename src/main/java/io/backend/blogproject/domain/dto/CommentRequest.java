package io.backend.blogproject.domain.dto;

public class CommentRequest {
    public record Create(
            String content
    ){}

    public record Update(
            String content
    ){}
}
