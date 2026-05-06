package io.backend.blogproject.controller;

import io.backend.blogproject.constant.Visibility;
import io.backend.blogproject.domain.dto.PostCreateRequest;
import io.backend.blogproject.domain.dto.PostUpdateRequest;
import io.backend.blogproject.domain.entity.Post;
import io.backend.blogproject.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 조회
    @GetMapping("/posts")
    public String list(Model model){
        model.addAttribute("posts", postService.getPublicPosts());

        return "posts";
    }

    //단건조회
    @GetMapping("/posts/{postId}")
    public String detail(@PathVariable Long postId, Model model){

        model.addAttribute("post", postService.getPost(postId));

        return "post_detail";
    }

    // 생성
    @GetMapping("posts/new")
    public String createForm(){
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
        return "redirect:/posts/"+postId;
    }


    // 삭제
    @PostMapping("/posts/{postId}/delete")
    public String deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return "redirect:/posts";
    }

}
