package io.backend.blogproject.controller;

import io.backend.blogproject.domain.dto.CommentRequest;
import io.backend.blogproject.domain.entity.DefaultCurrentUser;
import io.backend.blogproject.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments/{postId}/create")
    public String createComment(
            @AuthenticationPrincipal DefaultCurrentUser currentUser,
            @PathVariable Long postId,
            @RequestBody CommentRequest.Create request
    ) {
        commentService.createComment(currentUser.getUserId(), postId, request);
        return "redirect:/posts/%d".formatted(postId);
    }

    @PostMapping("/comments/{commentId}/reply")
    public String replyComment(
            @AuthenticationPrincipal DefaultCurrentUser currentUser,
            @PathVariable Long commentId,
            @RequestParam Long postId,
            @RequestBody CommentRequest.Create request
    ) {
        commentService.replyComment(
                currentUser.getUserId(),
                postId,
                commentId,
                request
        );
        return "redirect:/posts/%d".formatted(postId);
    }

    @PutMapping("/comments/{commentId}")
    @ResponseBody
    public ResponseEntity<String> updateComment(
            @AuthenticationPrincipal DefaultCurrentUser currentUser,
            @PathVariable Long commentId,
            @RequestBody CommentRequest.Update request
    ) {
        commentService.updateComment(currentUser.getUserId(), commentId, request);
        return ResponseEntity.ok("댓글이 수정되었습니다.");
    }

    @DeleteMapping("/comments/{commentId}")
    @ResponseBody
    public ResponseEntity<String> deleteComment(
            @AuthenticationPrincipal DefaultCurrentUser currentUser,
            @PathVariable Long commentId
    ) {
        commentService.deleteComment(currentUser.getUserId(), commentId);
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }
}
