package io.backend.blogproject.domain.dto;

import io.backend.blogproject.constant.Visibility;
import io.backend.blogproject.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostListResponse {

    private Long postId;
    private String title;
    private Long viewedCnt;
    private Visibility visibility;
    private LocalDateTime createdAt;

    public static PostListResponse from(Post post) {
        return new PostListResponse(
                post.getPostId(),
                post.getTitle(),
                post.getViewedCnt(),
                post.getVisibility(),
                post.getCreatedAt()
        );
    }
}
