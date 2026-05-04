package io.backend.blogproject.controller;

import io.backend.blogproject.domain.dto.CategoryCreateRequest;
import io.backend.blogproject.domain.dto.CategoryUpdateRequest;
import io.backend.blogproject.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("categories", categoryService.getCategories());
        return "categories/list";
    }

    @PostMapping
    public String create(CategoryCreateRequest request) {
        categoryService.createCategory(request);
        return "redirect:/categories";
    }

    @PostMapping("/{categoryId}/update")
    public String update(
            @PathVariable Long categoryId,
            CategoryUpdateRequest request
    ) {
        categoryService.updateCategory(categoryId, request);
        return "redirect:/categories";
    }

    @PostMapping("/{categoryId}/delete")
    public String delete(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return "redirect:/categories";
    }
}