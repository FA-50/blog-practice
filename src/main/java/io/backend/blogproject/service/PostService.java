package io.backend.blogproject.service;

import io.backend.blogproject.common.CustomException;
import io.backend.blogproject.constant.ErrorCode;
import io.backend.blogproject.constant.Status;
import io.backend.blogproject.constant.Visibility;
import io.backend.blogproject.domain.dto.PostRequest;
import io.backend.blogproject.domain.dto.PostResponse;
import io.backend.blogproject.domain.entity.Category;
import io.backend.blogproject.domain.entity.Members;
import io.backend.blogproject.domain.entity.Post;
import io.backend.blogproject.repository.CategoryRepository;
import io.backend.blogproject.repository.MemberRepository;
import io.backend.blogproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long createPost(Long memberId, PostRequest.Create request) {
        Category category = findCategoryOrNull(request.categoryId());
        Members member = memberRepository.findByIdOrThrow(memberId);

        Post post = Post.builder()
                .title(request.title())
                .content(request.content())
                .visibility(request.visibility())
                .category(category)
                .member(member)
                .build();

        return postRepository.save(post).getPostId();
    }

    public PostResponse.PostPage getPublicPosts(int page, Long categoryId, boolean noCategory) {
        int offsetPage = Math.max(page - 1, 0);
        PageRequest pageable = PageRequest.of(
                offsetPage,
                10,
                Sort.by(Sort.Direction.DESC, "createdAt", "postId")
        );

        org.springframework.data.domain.Page<Post> postPage;
        if (noCategory) {
            postPage = postRepository.findAllByCategoryIsNullAndStatusAndVisibility(
                    Status.ACTIVATED, Visibility.PUBLIC, pageable
            );
        } else if (categoryId != null) {
            postPage = postRepository.findAllByCategory_IdAndStatusAndVisibility(
                    categoryId, Status.ACTIVATED, Visibility.PUBLIC, pageable
            );
        } else {
            postPage = postRepository.findAllByStatusAndVisibility(
                    Status.ACTIVATED, Visibility.PUBLIC, pageable
            );
        }

        List<PostResponse.PostList> responses = postPage.getContent().stream()
                .map(PostResponse.PostList::from)
                .toList();

        return new PostResponse.PostPage(
                responses,
                page,
                postPage.getTotalPages(),
                postPage.getTotalElements(),
                postPage.hasPrevious(),
                postPage.hasNext()
        );
    }

    public PostResponse.PostDetail getPostWithoutViewCount(Long postId) {
        return PostResponse.PostDetail.from(findPublicPost(postId));
    }

    @Transactional
    public PostResponse.PostDetail getPost(Long postId) {
        Post post = findPublicPost(postId);
        post.increaseViewCount();
        return PostResponse.PostDetail.from(post);
    }

    public PostResponse.PostDetail getPostForEdit(Long memberId, Long postId) {
        Post post = findActivePost(postId);
        validateOwner(post, memberId);
        return PostResponse.PostDetail.from(post);
    }

    @Transactional
    public void updatePost(Long memberId, Long postId, PostRequest.Update request) {
        Post post = findActivePost(postId);
        validateOwner(post, memberId);

        post.update(
                request.title(),
                request.content(),
                request.visibility(),
                findCategoryOrNull(request.categoryId())
        );
    }

    @Transactional
    public void deletePost(Long memberId, Long postId) {
        Post post = findActivePost(postId);
        validateOwner(post, memberId);
        post.remove();
    }

    private Post findPublicPost(Long postId) {
        return postRepository.findByPostIdAndStatusAndVisibility(
                        postId, Status.ACTIVATED, Visibility.PUBLIC
                )
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }

    private Post findActivePost(Long postId) {
        return postRepository.findByPostIdAndStatus(postId, Status.ACTIVATED)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }

    private void validateOwner(Post post, Long memberId) {
        if (!post.isWrittenBy(memberId)) {
            throw new CustomException(ErrorCode.POST_ACCESS_DENIED);
        }
    }

    private Category findCategoryOrNull(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다. id=" + categoryId));
    }
}
