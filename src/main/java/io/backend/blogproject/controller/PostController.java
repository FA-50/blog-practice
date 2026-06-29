package io.backend.blogproject.controller;

import io.backend.blogproject.constant.Visibility;
import io.backend.blogproject.domain.dto.Page;
import io.backend.blogproject.domain.dto.PostRequest;
import io.backend.blogproject.domain.dto.PostResponse;
import io.backend.blogproject.domain.entity.DefaultCurrentUser;
import io.backend.blogproject.service.CategoryService;
import io.backend.blogproject.service.CommentService;
import io.backend.blogproject.service.PostService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CategoryService categoryService;
    private final CommentService commentService;

    @GetMapping("/posts")
    public String list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "false") boolean noCategory,
            Model model
    ) {
        PostResponse.PostPage response = postService.getPublicPosts(page, categoryId, noCategory);
        model.addAttribute("page", response);
        model.addAttribute("categories", categoryService.getCategories());
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("noCategory", noCategory);
        model.addAttribute("posts", response.posts());
        return "posts";
    }

    @GetMapping("/posts/{postId}")
    public String detail(
            @PathVariable Long postId,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "1") int page
    ) {
        Page comments = commentService.getComments(postId, size, page - 1);
        model.addAttribute("comments", comments.comments());
        model.addAttribute("page", comments);

        String cookieName = "viewed_post_" + postId;
        if (hasCookie(request, cookieName)) {
            model.addAttribute("post", postService.getPostWithoutViewCount(postId));
        } else {
            model.addAttribute("post", postService.getPost(postId));
            Cookie cookie = new Cookie(cookieName, "true");
            cookie.setMaxAge(60 * 5);
            cookie.setPath("/posts/" + postId);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
        }
        return "post_detail";
    }

    @GetMapping("/posts/new")
    public String createForm(Model model) {
        model.addAttribute("categories", categoryService.getCategories());
        return "post_form";
    }

    @PostMapping("/posts")
    public String createPost(
            @AuthenticationPrincipal DefaultCurrentUser currentUser,
            PostRequest.Create request
    ) {
        Long postId = postService.createPost(currentUser.getUserId(), request);
        return request.visibility() == Visibility.PRIVATE
                ? "redirect:/posts"
                : "redirect:/posts/" + postId;
    }

    @GetMapping("/posts/{postId}/edit")
    public String updateForm(
            @AuthenticationPrincipal DefaultCurrentUser currentUser,
            @PathVariable Long postId,
            Model model
    ) {
        model.addAttribute("post", postService.getPostForEdit(currentUser.getUserId(), postId));
        model.addAttribute("categories", categoryService.getCategories());
        return "post_edit";
    }

    @PutMapping("/posts/{postId}")
    @ResponseBody
    public ResponseEntity<String> updatePost(
            @AuthenticationPrincipal DefaultCurrentUser currentUser,
            @PathVariable Long postId,
            @RequestBody PostRequest.Update request
    ) {
        postService.updatePost(currentUser.getUserId(), postId, request);
        return ResponseEntity.ok("게시글이 수정되었습니다.");
    }

    @DeleteMapping("/posts/{postId}")
    @ResponseBody
    public ResponseEntity<String> deletePost(
            @AuthenticationPrincipal DefaultCurrentUser currentUser,
            @PathVariable Long postId
    ) {
        postService.deletePost(currentUser.getUserId(), postId);
        return ResponseEntity.ok("게시글이 삭제되었습니다.");
    }

    private boolean hasCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return false;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return true;
            }
        }
        return false;
    }
}
