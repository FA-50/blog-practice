package io.backend.blogproject.repository;

import io.backend.blogproject.constant.Status;
import io.backend.blogproject.constant.Visibility;
import io.backend.blogproject.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @EntityGraph(attributePaths = {"category", "member"})
    Page<Post> findAllByStatusAndVisibility(Status status, Visibility visibility, Pageable pageable);

    @EntityGraph(attributePaths = {"category", "member"})
    Page<Post> findAllByCategory_IdAndStatusAndVisibility(
            Long categoryId,
            Status status,
            Visibility visibility,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"category", "member"})
    Page<Post> findAllByCategoryIsNullAndStatusAndVisibility(
            Status status,
            Visibility visibility,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"category", "member"})
    Optional<Post> findByPostIdAndStatus(Long postId, Status status);

    @EntityGraph(attributePaths = {"category", "member"})
    Optional<Post> findByPostIdAndStatusAndVisibility(
            Long postId,
            Status status,
            Visibility visibility
    );

    @Modifying(flushAutomatically = true)
    @Query("update Post p set p.category = null where p.category.id = :categoryId")
    void clearCategoryByCategoryId(@Param("categoryId") Long categoryId);
}
