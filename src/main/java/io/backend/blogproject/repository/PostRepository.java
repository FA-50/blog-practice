package io.backend.blogproject.repository;

import io.backend.blogproject.constant.Status;
import io.backend.blogproject.constant.Visibility;
import io.backend.blogproject.domain.entity.Post;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final EntityManagerFactory emf;

    public Post save(Post post){
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try{
            tx.begin();

            em.persist(post);

            tx.commit();

            return post;
        } catch (Exception e){
            if (tx.isActive()) {
                tx.rollback();
            }

            throw new RuntimeException("게시글 저장에 실패했습니다.", e);
        } finally {
            em.close();
        }

    }

    public List<Post> findAllByStatus(Status status) {
        try (EntityManager em = emf.createEntityManager()) {
            String jpql = """
                    SELECT p
                    FROM Post p
                    WHERE p.status = :status
                    """;

            return em.createQuery(jpql, Post.class)
                    .setParameter("status", status)
                    .getResultList();
        } catch (Exception e) {
            throw new RuntimeException("게시글 목록 조회에 실패했습니다.", e);
        }
    }

    public List<Post> findAllByStatusAndVisibility(Status status, Visibility visibility) {
        try (EntityManager em = emf.createEntityManager()) {
            String jpql = """
                    SELECT p
                    FROM Post p
                    WHERE p.status = :status
                    AND p.visibility = :visibility
                    """;

            return em.createQuery(jpql, Post.class)
                    .setParameter("status", status)
                    .setParameter("visibility", visibility)
                    .getResultList();
        } catch (Exception e) {
            throw new RuntimeException("공개 게시글 목록 조회에 실패했습니다.", e);
        }
    }

    public Optional<Post> findByPostIdAndStatus(Long postId, Status status) {
        try (EntityManager em = emf.createEntityManager()) {
            String jpql = """
                    SELECT p
                    FROM Post p
                    LEFT JOIN FETCH p.category
                    WHERE p.postId = :postId
                    AND p.status = :status
                    """;

            List<Post> result = em.createQuery(jpql, Post.class)
                    .setParameter("postId", postId)
                    .setParameter("status", status)
                    .getResultList();

            return result.stream().findFirst();
        } catch (Exception e) {
            throw new RuntimeException("게시글 조회에 실패했습니다.", e);
        }
    }

    public Optional<Post> findByPostIdAndStatusAndVisibility(
            Long postId,
            Status status,
            Visibility visibility
    ) {
        try (EntityManager em = emf.createEntityManager()) {
            String jpql = """
                SELECT p
                FROM Post p
                LEFT JOIN FETCH p.category
                WHERE p.postId = :postId
                AND p.status = :status
                AND p.visibility = :visibility
                """;

            List<Post> result = em.createQuery(jpql, Post.class)
                    .setParameter("postId", postId)
                    .setParameter("status", status)
                    .setParameter("visibility", visibility)
                    .getResultList();

            return result.stream().findFirst();
        } catch (Exception e) {
            throw new RuntimeException("게시글 조회에 실패했습니다.", e);
        }
    }

    public void update(Post post) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            em.merge(post);

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }

            throw new RuntimeException("게시글 수정에 실패했습니다.", e);
        } finally {
            em.close();
        }
    }
}
