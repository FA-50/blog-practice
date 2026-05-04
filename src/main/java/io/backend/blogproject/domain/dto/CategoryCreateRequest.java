package io.backend.blogproject.domain.dto;

public class CategoryCreateRequest {
    private String title;

    public CategoryCreateRequest() {
    }

    public CategoryCreateRequest(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
