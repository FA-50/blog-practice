package io.backend.blogproject.service;

import io.backend.blogproject.common.CustomException;
import io.backend.blogproject.constant.ErrorCode;
import io.backend.blogproject.constant.Status;
import io.backend.blogproject.constant.Visibility;
import io.backend.blogproject.domain.dto.CommentRequest;
import io.backend.blogproject.domain.dto.CommentResponse;
import io.backend.blogproject.domain.dto.Page;
import io.backend.blogproject.domain.entity.Comment;
import io.backend.blogproject.domain.entity.Members;
import io.backend.blogproject.domain.entity.Post;
import io.backend.blogproject.repository.CommentRepository;
import io.backend.blogproject.repository.MemberRepository;
import io.backend.blogproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public Page getComments(Long postId, int size, int page) {
        List<Comment> comments = commentRepository.findAllByPost_PostIdOrderByIdAsc(postId);
        List<CommentResponse> responses = comments.stream()
                .map(comment -> CommentResponse.of(
                        comment.getId(),
                        comment.getContent(),
                        findRoot(comment),
                        comment.getStatus(),
                        comment.getCreatedAt(),
                        comment.getChildId(),
                        comment.getParentId(),
                        comment.getMember().getMemberId(),
                        comment.getMember().getName()
                ))
                .sorted(Comparator.comparing(CommentResponse::rootId)
                        .thenComparing(CommentResponse::commentId))
                .skip((long) size * page)
                .limit(size)
                .toList();

        long totalSize = comments.size();
        int totalPages = (int) Math.ceil((double) totalSize / size);
        return new Page(size, page, totalSize, totalPages, responses);
    }

    @Transactional
    public void createComment(Long memberId, Long postId, CommentRequest.Create request) {
        Members member = memberRepository.findByIdOrThrow(memberId);
        Post post = findPublicPost(postId);
        commentRepository.save(Comment.createComment(post, request.content(), member));
    }

    @Transactional
    public void replyComment(
            Long memberId,
            Long postId,
            Long commentId,
            CommentRequest.Create request
    ) {
        Members member = memberRepository.findByIdOrThrow(memberId);
        Post post = findPublicPost(postId);
        Comment parentComment = findActiveComment(commentId);

        if (!parentComment.belongsTo(postId)) {
            throw new CustomException(ErrorCode.COMMENT_POST_MISMATCH);
        }

        commentRepository.save(Comment.replyComment(
                post,
                request.content(),
                parentComment,
                member
        ));
    }

    public Comment getCommentById(Long commentId) {
        return findActiveComment(commentId);
    }

    @Transactional
    public void updateComment(Long memberId, Long commentId, CommentRequest.Update request) {
        findOwnedComment(memberId, commentId).update(request.content());
    }

    @Transactional
    public void deleteComment(Long memberId, Long commentId) {
        findOwnedComment(memberId, commentId).delete();
    }

    private Post findPublicPost(Long postId) {
        return postRepository.findByPostIdAndStatusAndVisibility(
                        postId, Status.ACTIVATED, Visibility.PUBLIC
                )
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }

    private Comment findOwnedComment(Long memberId, Long commentId) {
        Comment comment = findActiveComment(commentId);
        if (!comment.isWrittenBy(memberId)) {
            throw new CustomException(ErrorCode.COMMENT_ACCESS_DENIED);
        }
        return comment;
    }

    private Comment findActiveComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.UNABLE_TO_FIND_COMMENT));
        if (comment.getStatus() == Status.REMOVED) {
            throw new CustomException(ErrorCode.ALREADY_DELETED);
        }
        return comment;
    }

    private Long findRoot(Comment comment) {
        Comment current = comment;
        while (current.getParentId() != null) {
            current = current.getParentId();
        }
        return current.getId();
    }
}
