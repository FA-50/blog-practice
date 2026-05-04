package io.backend.blogproject.repository;

import io.backend.blogproject.constant.ErrorCode;
import io.backend.blogproject.constant.Status;
import io.backend.blogproject.domain.entity.Comment;
import io.backend.blogproject.domain.entity.Post;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepository {
    private final EntityManagerFactory emf;

    public List<Comment> findCommentsByPostId(Long postId){
        try(
                EntityManager em = emf.createEntityManager()
        ) {
            String JPQL_GET_COMMENTS = """
                    SELECT c
                    FROM Comment c
                    WHERE c.post.id = :postId
                    AND c.status != 'REMOVED'
                    """;
            return em.createQuery(JPQL_GET_COMMENTS, Comment.class)
                    .setParameter("postId", postId)
                    .getResultList();
        } catch(Exception e) {
            throw new RuntimeException(ErrorCode.UNABLE_TO_FIND_COMMENT.message,e);
        }
    }

    public Comment findCommentByCommentId(Long commentId){
        try(
                EntityManager em = emf.createEntityManager()
        ) {
            String JPQL_GET_COMMENTS = """
                    SELECT c
                    FROM Comment c
                    WHERE c.id = :commentId
                    """;
            Comment foundedComment = em.createQuery(JPQL_GET_COMMENTS, Comment.class)
                    .setParameter("commentId", commentId)
                    .getSingleResult();

            if (foundedComment.getStatus().equals(Status.REMOVED)) throw new RuntimeException(ErrorCode.ALREADY_DELETED.message);
            if (foundedComment==null) throw new RuntimeException(ErrorCode.UNABLE_TO_FIND_COMMENT.message);
            return foundedComment;
        } catch(Exception e) {
            throw new RuntimeException(ErrorCode.UNABLE_TO_FIND_COMMENT.message,e);
        }
    }

    public void createComment(
            Post post,
            String content
    ){
        try(
                EntityManager em = emf.createEntityManager()
        ) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();

            Comment comment = Comment.createComment(
                    post,
                    content
            );

            em.persist(comment);
            tx.commit();
        } catch (Exception e){
            throw new RuntimeException(ErrorCode.NO_POST.message,e);
        }
    }

    public void replyComment(
            Post post,
            Comment parentComment,
            String content
    ){
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Comment comment = Comment.replyComment(
                    post,
                    content,
                    parentComment
            );

            em.persist(comment);
            tx.commit();
        } catch (Exception e){
            tx.rollback();
            throw new RuntimeException(ErrorCode.CAN_NOT_CREATE_COMMENT.message,e);
        } finally {
            em.close();
        }
    }

    public void deleteComment(Comment comment){
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            comment.delete();
            em.merge(comment);
            tx.commit();
        } catch (Exception e){
            tx.rollback();
            throw new RuntimeException(ErrorCode.CAN_NOT_CREATE_COMMENT.message,e);
        } finally {
            em.close();
        }
    }
}
