package io.backend.blogproject.controller;

import io.backend.blogproject.constant.Visibility;
import io.backend.blogproject.domain.entity.Post;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PostController {

    private final List<Post> posts = new ArrayList<>();
    Long sequence = 1L;
    public PostController(){
        posts.add(new Post(sequence++, "첫 번째 게시글", "첫 번째 게시글 내용입니다.", Visibility.PUBLIC));
        posts.add(new Post(sequence++, "두 번째 게시글", "두 번째 게시글 내용입니다.", Visibility.PUBLIC));
        posts.add(new Post(sequence++, "세 번째 게시글", "세 번째 게시글 내용입니다.", Visibility.PUBLIC));

    }

    @GetMapping("posts")
    public String list(Model model){
        model.addAttribute("posts", posts);

        return "posts";
    }

    @GetMapping("/posts/{id}")
    public String detail(@PathVariable Long id, Model model){
        Post foundPost = null;

        for (Post post : posts) {
            if (post.getPostId().equals(id)) {
                foundPost = post;
                break;
            }
        }

        if (foundPost == null) {
            throw new IllegalArgumentException("게시글을 찾을 수 없습니다. id=" + id);
        }

        model.addAttribute("post", foundPost);

        return "post_detail";
    }


}
