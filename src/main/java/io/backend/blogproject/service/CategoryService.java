package io.backend.blogproject.service;

import io.backend.blogproject.domain.dto.CategoryCreateRequest;
import io.backend.blogproject.domain.dto.CategoryUpdateRequest;
import io.backend.blogproject.domain.entity.Category;
import io.backend.blogproject.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private static CategoryRepository categoryRepository = null;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public static List<Category> getCategories() {
        return categoryRepository.findAllActivated();
    }

    @Transactional
    public void createCategory(CategoryCreateRequest request) {
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new IllegalArgumentException("카테고리 이름을 작성해주세용.");
        }

        Category category = new Category(request.getTitle());
        categoryRepository.save(category);
    }

    @Transactional
    public void updateCategory(Long categoryId, CategoryUpdateRequest request) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니데."));

        category.update(request.getTitle());

        categoryRepository.update(category);
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

        category.softDelete();

        categoryRepository.update(category);
    }
}