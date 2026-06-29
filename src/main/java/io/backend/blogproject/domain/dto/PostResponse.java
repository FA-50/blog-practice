package io.backend.blogproject.domain.dto;

import io.backend.blogproject.constant.Visibility;
import io.backend.blogproject.domain.entity.Post;

import java.time.LocalDateTime;
import java.util.List;

public class PostResponse {
    public record PostDetail(
            Long postId,
            String title,
            String content,
            Long viewedCnt,
            Visibility visibility,
            Long categoryId,
            String categoryTitle,
            Long memberId,
            String memberName,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        public static PostDetail from(Post post) {
            Long categoryId = null;
            String categoryTitle = null;

            if (post.getCategory() != null) {
                categoryId = post.getCategory().getId();
                categoryTitle = post.getCategory().getTitle();
            }

            return new PostDetail(
                    post.getPostId(),
                    post.getTitle(),
                    post.getContent(),
                    post.getViewedCnt(),
                    post.getVisibility(),
                    categoryId,
                    categoryTitle,
                    post.getMember().getMemberId(),
                    post.getMember().getName(),
                    post.getCreatedAt(),
                    post.getUpdatedAt()
            );
        }
    }

    public record PostList(
            Long postId,
            String title,
            Long viewedCnt,
            Visibility visibility,
            Long categoryId,
            String categoryTitle,
            Long memberId,
            String memberName,
            LocalDateTime createdAt
    ) {
        public static PostList from(Post post) {
            Long categoryId = null;
            String categoryTitle = "카테고리 없음";

            if (post.getCategory() != null) {
                categoryId = post.getCategory().getId();
                categoryTitle = post.getCategory().getTitle();
            }

            return new PostList(
                    post.getPostId(),
                    post.getTitle(),
                    post.getViewedCnt(),
                    post.getVisibility(),
                    categoryId,
                    categoryTitle,
                    post.getMember().getMemberId(),
                    post.getMember().getName(),
                    post.getCreatedAt()
            );
        }
    }

    public record PostPage(
            List<PostList> posts,
            int currentPage,
            int totalPages,
            long totalElements,
            boolean hasPrevious,
            boolean hasNext
    ) {
    }
}
