package io.backend.blogproject.controller;

import io.backend.blogproject.constant.Visibility;
import io.backend.blogproject.domain.entity.Post;
import io.backend.blogproject.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/posts")
    public String list(Model model){
        model.addAttribute("posts", postService.getPublicPosts());

        return "posts";
    }

    @GetMapping("/posts/{postId}")
    public String detail(@PathVariable Long postId, Model model){

        model.addAttribute("post", postService.getPost(postId));

        return "post_detail";
    }


}
