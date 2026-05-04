package io.backend.blogproject.service;

import io.backend.blogproject.domain.entity.Comment;
import io.backend.blogproject.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public List<Comment> getComments(Long postId){
        return commentRepository.findCommentsByPostId(postId);
    }

    public Comment getCommentById(Long commentId){
        return commentRepository.findCommentByCommentId(commentId);
    }

    public void deleteComment(Long commentId){
        Comment foundedComment = commentRepository.findCommentByCommentId(commentId);
        commentRepository.deleteComment(foundedComment);
    }
}










