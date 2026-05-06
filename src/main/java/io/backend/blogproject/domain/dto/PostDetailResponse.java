package io.backend.blogproject.domain.dto;

import io.backend.blogproject.constant.Visibility;
import io.backend.blogproject.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostDetailResponse {

    private Long postId;
    private String title;
    private String content;
    private Long viewedCnt;
    private Visibility visibility;
    private Long categoryId;
    private String categoryTitle;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PostDetailResponse from(Post post) {
        Long categoryId = null;
        String categoryTitle = null;

        if (post.getCategory() != null) {
            categoryId = post.getCategory().getId();
            categoryTitle = post.getCategory().getTitle();
        }

        return new PostDetailResponse(
                post.getPostId(),
                post.getTitle(),
                post.getContent(),
                post.getViewedCnt(),
                post.getVisibility(),
                categoryId,
                categoryTitle,
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}
