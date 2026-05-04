package io.backend.blogproject.domain.dto;

public class CategoryUpdateRequest {
    private String title;

    public CategoryUpdateRequest() {
    }

    public CategoryUpdateRequest(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
