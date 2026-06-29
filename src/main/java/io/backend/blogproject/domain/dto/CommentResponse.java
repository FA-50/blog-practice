package io.backend.blogproject.domain.dto;

import io.backend.blogproject.constant.Status;
import io.backend.blogproject.domain.entity.Comment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record CommentResponse(
        Long commentId,
        String content,
        String createdAt,
        Status status,
        Long rootId,
        Long childId,
        Long parentId,
        Long memberId,
        String memberName
) {
    public static CommentResponse of(
            Long commentId,
            String content,
            Long rootId,
            Status status,
            LocalDateTime createdAt,
            Comment childId,
            Comment parentId,
            Long memberId,
            String memberName
    ) {
        return new CommentResponse(
                commentId,
                content,
                createdAt.format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                status,
                rootId,
                childId == null ? null : childId.getId(),
                parentId == null ? null : parentId.getId(),
                memberId,
                memberName
        );
    }
}
