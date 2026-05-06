package io.backend.blogproject.domain.dto;

import io.backend.blogproject.constant.Visibility;
import io.backend.blogproject.domain.entity.Post;
import io.backend.blogproject.service.CategoryService;
import io.backend.blogproject.service.PostService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

    @GetMapping("/posts")
    public String list(Model model){
        model.addAttribute("posts", PostService.getPublicPosts());
        model.addAttribute("categories", CategoryService.getCategories());

        return "posts";
    }
}
