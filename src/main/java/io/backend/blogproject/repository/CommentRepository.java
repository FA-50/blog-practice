package io.backend.blogproject.repository;

import io.backend.blogproject.domain.entity.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"parentId", "childId", "member"})
    List<Comment> findAllByPost_PostIdOrderByIdAsc(Long postId);
}
