package io.backend.blogproject.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PostPageResponse {

    private List<PostListResponse> posts;

    private int currentPage;
    private int totalPages;
    private long totalElements;

    private boolean hasPrevious;
    private boolean hasNext;
}
