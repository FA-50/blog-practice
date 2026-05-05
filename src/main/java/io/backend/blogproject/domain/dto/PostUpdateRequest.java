package io.backend.blogproject.domain.dto;

import io.backend.blogproject.constant.Visibility;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostUpdateRequest {

    private String title;
    private String content;
    private Visibility visibility;
    private Long categoryId;
}
