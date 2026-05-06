package io.backend.blogproject.controller;

import io.backend.blogproject.constant.Visibility;
import io.backend.blogproject.domain.dto.PostCreateRequest;
import io.backend.blogproject.domain.dto.PostPageResponse;
import io.backend.blogproject.domain.dto.PostUpdateRequest;
import io.backend.blogproject.domain.entity.Post;
import io.backend.blogproject.service.CategoryService;
import io.backend.blogproject.service.PostService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CategoryService categoryService;

    // 조회
    @GetMapping("/posts")
    public String list(
            @RequestParam(defaultValue = "1") int page,
            Model model
    ){
        PostPageResponse response = postService.getPublicPosts(page);

        model.addAttribute("page", response);
        model.addAttribute("posts", response.getPosts());

        return "posts";
    }

    //단건조회
    @GetMapping("/posts/{postId}")
    public String detail(
            @PathVariable Long postId,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response){

        String cookieName = "viewed_post_" + postId;

        boolean alreadyViewed = hasCookie(request, cookieName);

        if(alreadyViewed){
            model.addAttribute("post",postService.getPostWithoutViewCount(postId));
        }else{
            model.addAttribute("post", postService.getPost(postId));

            Cookie cookie = new Cookie(cookieName, "true");
            cookie.setMaxAge(60*5); // 5분
            cookie.setPath("/posts/"+postId);
            cookie.setHttpOnly(true);

            response.addCookie(cookie);
        }

        return "post_detail";
    }

    // 생성
    @GetMapping("/posts/new")
    public String createForm(Model model){
        model.addAttribute("categories", categoryService.getCategories());
        return "post_form";
    }

    @PostMapping("/posts")
    public String createPost(PostCreateRequest request){
        Long postId = postService.createPost(request);

        if(request.getVisibility() == Visibility.PRIVATE){
            return "redirect:/posts";
        }

        return "redirect:/posts/" + postId;
    }

    // 수정
    @GetMapping("/posts/{postId}/edit")
    public String updateForm(@PathVariable Long postId, Model model) {
        model.addAttribute("post", postService.getPostForEdit(postId));
        return "post_edit";
    }

    @PostMapping("posts/{postId}/edit")
    public String updateForm(@PathVariable Long postId, PostUpdateRequest request){
        postService.updatePost(postId, request);;

        if(request.getVisibility() == Visibility.PRIVATE){
            return "redirect:/posts";
        }

        return "redirect:/posts/"+postId;
    }


    // 삭제
    @PostMapping("/posts/{postId}/delete")
    public String deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return "redirect:/posts";
    }


    // 쿠키확인 메서드
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
