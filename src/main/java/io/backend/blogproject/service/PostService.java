package io.backend.blogproject.service;

import io.backend.blogproject.constant.Status;
import io.backend.blogproject.constant.Visibility;
import io.backend.blogproject.domain.dto.*;
import io.backend.blogproject.domain.entity.Category;
import io.backend.blogproject.domain.entity.Post;
import io.backend.blogproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    //private final CategoryRepository categoryRepository;

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

    public PostPageResponse getPublicPosts(int page){
        int offsetPage = page - 1;
        int size = 10;

        if(offsetPage < 0){
            offsetPage = 0;
        }


        List<Post> posts = postRepository.findAllByStatusAndVisibility(
                Status.ACTIVATED,
                Visibility.PUBLIC,
                offsetPage,
                size
        );

        long totalElements = postRepository.countByStatusAndVisibility(
                Status.ACTIVATED,
                Visibility.PUBLIC
        );

        int totalPages = (int)Math.ceil((double) totalElements / size);


        List<PostListResponse> postResponses = posts.stream()
                .map(PostListResponse::from)
                .toList();



        return new PostPageResponse(
                postResponses,
                page,
                totalPages,
                totalElements,
                page>1,
                page<totalPages
        );
    }

    public PostDetailResponse getPostWithoutViewCount(Long postId){
        Post post = postRepository.findByPostIdAndStatusAndVisibility(postId, Status.ACTIVATED, Visibility.PUBLIC)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. id="+postId));

        return PostDetailResponse.from(post);
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

//        return categoryRepository.findById(categoryId)
//                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다. id=" + categoryId));

        return null;
    }
}