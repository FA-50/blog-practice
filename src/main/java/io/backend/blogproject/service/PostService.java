package io.backend.blogproject.service;

import io.backend.blogproject.constant.Status;
import io.backend.blogproject.constant.Visibility;
import io.backend.blogproject.domain.dto.PostCreateRequest;
import io.backend.blogproject.domain.dto.PostDetailResponse;
import io.backend.blogproject.domain.dto.PostListResponse;
import io.backend.blogproject.domain.dto.PostUpdateRequest;
import io.backend.blogproject.domain.entity.Category;
import io.backend.blogproject.domain.entity.Post;
import io.backend.blogproject.repository.CategoryRepository;
import io.backend.blogproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;

    public Long createPost(PostCreateRequest request){
        Category category = findCategoryOrNull(request.getCategoryId());

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .visibility(request.getVisibility())
                .category(category)
                .build();

        Post savedPost = postRepository.save(post);

        return savedPost.getPostId();
    }

    public List<PostListResponse> getPublicPosts(){
        List<Post> posts = postRepository.findAllByStatusAndVisibility(
                Status.ACTIVATED,
                Visibility.PUBLIC
        );


        return posts.stream()
                .map(PostListResponse::from)
                .toList();
    }

    public PostDetailResponse getPost(Long postId){
        Post post = postRepository.findByPostIdAndStatusAndVisibility(postId, Status.ACTIVATED, Visibility.PUBLIC)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. id="+postId));
        post.increaseViewCount();
        postRepository.update(post);

        return PostDetailResponse.from(post);
    }

    public PostDetailResponse getPostForEdit(Long postId) {
        Post post = postRepository.findByPostIdAndStatus(postId, Status.ACTIVATED)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. id=" + postId));

        return PostDetailResponse.from(post);
    }

    public void updatePost(Long postId, PostUpdateRequest request){
        Post post = postRepository.findByPostIdAndStatus(postId, Status.ACTIVATED)
                .orElseThrow(()->new IllegalArgumentException("게시글을 찾을 수 없습니다. id="+postId));


        Category category = findCategoryOrNull(request.getCategoryId());


        post.update(
                request.getTitle(),
                request.getContent(),
                request.getVisibility(),
                category
        );
        postRepository.update(post);
    }

    public void deletePost(Long postId){
        Post post = postRepository.findByPostIdAndStatus(postId, Status.ACTIVATED)
                .orElseThrow(()-> new IllegalArgumentException("게시글을 찾을 수 없습니다. id="+postId));

        post.remove();
        postRepository.update(post);
    }

    private Category findCategoryOrNull(Long categoryId) {
        if (categoryId == null) {
            return null;
        }

        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다. id=" + categoryId));

    }
}